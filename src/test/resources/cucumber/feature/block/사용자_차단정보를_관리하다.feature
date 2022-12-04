# language: ko
기능: 사용자 차단관리
  사용자 차단하기, 차단목록 조회정보를 관리한다

  @block-save
  시나리오 개요: 사용자 차단하기
    먼저 사용자차단을 위한 "<docs>""<blockAccountId>" 가 있다
    만약 사용자차단API를 요청하면
    그러면 사용자차단API 호출결과를 확인한다

    예:
    | docs                        | blockAccountId  |
    | /block/block-save-mutation  | TEST1           |
    | /block/block-save-mutation  | TEST2           |