# language: ko
기능: 로그인 기본정보 조회
  소셜로그인시 필요한 기본정보를 조회한다

  @auth
  시나리오 개요: 로그인 기본정보 조회
    먼저 로그인 기본정보 조회를 위한 "<docs>" 가 있다
    만약 로그인 기본정보 조회API를 요청하면
    그러면 호출결과 "<redirectUri>""<state>""<jsKey>""<nativeKey>" 를 확인한다

    예:
    | docs                | redirectUri     | state     | jsKey                               | nativeKey                           |
    | /auth/auth-query    | /login/token    | kakao     | e09f051b266e433cd117edb6aaf588c3    | ef16d82d4bc4c7ed9c6dae9ac0931cc4    |
    | /auth/auth-query    | /login/token    | naver     | 7rZMvLkTIXowUlkv7h_y                | 7rZMvLkTIXowUlkv7h_y                |