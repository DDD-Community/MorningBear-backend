# language: ko
기능: 카테고리정보 관리
  카테고리 정보를 관리한다

  @category-findAll
  시나리오 개요: 카테고리정보 조회
    먼저 카테고리 정보를 조회하기 위한 "<docs>" 가 있다
    만약 카테고리 조회API를 요청하면
    그러면 카테고리 조회API 호출결과를 확인한다

    예:
    | docs                            |
    | /category/category-findAll      |

  @category-save
  시나리오 개요: 내 카테고리정보 저장
    먼저 카테고리 정보를 저장하기 위한 "<docs>""<categoryId>" 가 있다
    만약 내 카테고리정보 저장API를 요청하면
    그러면 내 카테고리정보 저장API 호출결과를 확인한다

    예:
    | docs                              | categoryId              |
    | /category/category-save           | TEST_CATEGORY1          |