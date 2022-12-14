# language: ko
기능: 내정보 관리
  내정보를 관리한다

  @myInfo-save
  시나리오 개요: 내정보 저장
    먼저 내정보를 저장하기 위한 "<docs>""<nickName>""<photoLink>""<memo>""<wakeUpAt>" 가 있다
    만약 내정보 저장API를 요청하면
    그러면 내정보 저장API 호출결과를 확인한다

    예:
    | docs                            | nickName    | photoLink   | memo        | wakeUpAt    |
    | /myinfo/myinfo-save-mutation    | TEST2       | TEST        | 자기소개TEST  | 0915        |
    | /myinfo/myinfo-save-mutation    | TEST        | TEST        | 자기소개TEST  | 0915        |

  @myInfo-find
  시나리오 개요: 내정보 조회
    먼저 내정보를 조회하기 위한 "<docs>" 가 있다
    만약 내정보 조회API를 요청하면
    그러면 호출결과 "<accountId>""<nickName>""<photoLink>""<memo>""<wakeUpAt>" 를 확인한다

    예:
    | docs                            | accountId     | nickName    | photoLink     | memo          | wakeUpAt    |
    | /myinfo/myinfo-find-query       | k::2524113454 | TEST        | TEST          | 자기소개TEST    | 0915        |

  @myInfo-delete
  시나리오 개요: 탈퇴하기
    먼저 내정보 탈퇴를 위한 "<docs>" 가 있다
    만약 탈퇴하기 API를 요청하면
    그러면 호출결과 "<response>" 를 확인한다

    예:
    | docs                            | response    |
    | /myinfo/myinfo-delete-mutation  | true        |