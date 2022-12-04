# language: ko
기능: 뱃지정보 관리
  뱃지 정보를 관리한다

  @badge-findAll
  시나리오 개요: 뱃지정보 조회
  먼저 뱃지 정보를 조회하기 위한 "<docs>" 가 있다
  만약 뱃지 조회API를 요청하면
  그러면 뱃지 조회API 호출결과 "<badgeId>""<badgeDesc>"<badgeTier> 를 확인한다

    예:
      | docs                            | badgeId       | badgeDesc     | badgeTier   |
      | /badge/badge-findAll            | TEST_BADGE1   | TEST_BADGE1   | 1           |
      | /badge/badge-findAll            | TEST_BADGE2   | TEST_BADGE2   | 1           |

  @badge-save
  시나리오 개요: 내 뱃지정보 저장
  먼저 뱃지 정보를 저장하기 위한 "<docs>""<badgeId>" 가 있다
  만약 내 뱃지정보 저장API를 요청하면
  그러면 내 뱃지정보 저장API 호출결과를 확인한다

    예:
      | docs                        | badgeId              |
      | /badge/badge-save           | TEST_BADGE1          |
      | /badge/badge-save           | TEST_BADGE2          |