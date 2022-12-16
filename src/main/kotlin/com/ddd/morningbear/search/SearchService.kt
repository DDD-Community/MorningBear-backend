package com.ddd.morningbear.search

import com.ddd.morningbear.common.constants.CommCode
import com.ddd.morningbear.common.exception.BadRequestException
import com.ddd.morningbear.common.exception.GraphQLBadRequestException
import com.ddd.morningbear.common.exception.ThirdPartyServerException
import com.ddd.morningbear.common.utils.AppPropsUtils
import com.ddd.morningbear.search.dto.SearchDto
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono
import javax.annotation.PostConstruct

/**
 * @author yoonho
 * @since 2022.12.14
 */
@Service
class SearchService(
    private val webClientBuilder: WebClient.Builder
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    private lateinit var webClient: WebClient

    @PostConstruct
    fun initWebClient() {
        this.webClient = webClientBuilder.build()
    }

    /**
     * 아티클 조회하기
     *
     * @param size [Int]
     * @return List [SearchDto.SearchItem]
     * @author yoonho
     * @since 2022.12.16
     */
    fun searchArticle(size: Int): List<SearchDto.SearchItem>? {
        try{
            val appconfig = AppPropsUtils.findClientInfoByType(CommCode.Social.NAVER.code)

            val params: MultiValueMap<String, String> = LinkedMultiValueMap()
            params.add("query", "miraclemorning")
            params.add("display", size.toString())

            val header: MultiValueMap<String, String> = LinkedMultiValueMap()
            header.add("X-Naver-Client-Id", appconfig?.get("clientId"))
            header.add("X-Naver-Client-Secret", appconfig?.get("clientSecret"))

            val uriComponents = UriComponentsBuilder
                .fromHttpUrl(AppPropsUtils.findUrl("napi") + "/v1/search/blog.json")
                .queryParams(params)
                .build(false)

            val responseEntity = webClient.get()
                .uri(uriComponents.toUri())
                .header("X-Naver-Client-Id", appconfig?.get("clientId"))
                .header("X-Naver-Client-Secret", appconfig?.get("clientSecret"))
                .acceptCharset(Charsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError) { Mono.error(BadRequestException()) }
                .onStatus(HttpStatus::is5xxServerError) { Mono.error(ThirdPartyServerException("napi internal server error")) }
                .toEntity(SearchDto::class.java)
                .block()

            return this.removeTag(responseEntity?.body!!.items)
        }catch (e: Exception){
            throw GraphQLBadRequestException()
        }
    }

    /**
     * 불필요한 HTML태그 삭제
     *
     * @param params [SearchDto.SearchItem]
     * @return List [SearchDto.SearchItem]
     * @author yoonho
     * @since 2022.12.16
     */
    private fun removeTag(params: List<SearchDto.SearchItem>?): List<SearchDto.SearchItem>? {
        params?.map {
            it.title = it.title?.replace("<b>", "")?.replace("</b>", "")
            it.description = it.description?.replace("<b>", "")?.replace("</b>", "")
        }
        return params
    }
}