# OMESS
## 기획 배경
개발 프로젝트 진행을 하며, 필요한 문서들과 채팅을 위한 협업 툴들이 모두 분리되어 있어 이를 일원화하여 보다 편리한 프로젝트 단위의 개발을 보조해줄 SW를 제작하고자 함.

## 

# Omess 정리

## Module

### Spring

- 사용자가 프로젝트 구조 파악을 보다 쉽게 하여 원활한 기능 추가를 돕기 위해 트리 형태의 테이블 구조 설계
- 트리 형태의 테이블 구조에 부모 테이블의 경로를 기록하는 path 컬럼을 추가하여 유저가 보낸 요청에 대한 유효성 검사를 path를 통해 검사하도록 하여 DB 최적화
    - ex) 1번 프로젝트의 2번 칸반보드에 3번 이슈에 대한 수정 요청 → api/v1/projects/1/kanbanboards/2/issues/3 으로 요청을 보내면 issue 테이블에서 pk 값이 3인 이슈를 조회 후 path 칼럼의 값이 P1/K2/I3인지 확인 수 서비스 로직 동작

## KanbanBoard

### Spring

- 여러명의 유저가 같은 칸반보드를 바라볼 때 동기화를 위해 stomp를 사용하여 칸반보드 내부의 이벤트 발생 시 http 요청 처리 후 websocket 요청을 한번 더 보내 같은 칸반보드를 바라보고있는 유저들에게 이벤트 발생과 최신화된 issue 목록을 보내줌

### React

- 마크 다운 에디터를 통해 이슈 내용 작성 할 수 있도록 화면 구현
- 이슈 이벤트 발생 후 서버로 부터 최신 이슈 목록을 받아오면 사용자가 선택한 필터 조건에 맞춰 이슈 리스트를 필터링 후 화면에 뿌려준다

맡은 기능 백엔드 
 - 프로젝트 
    - 생성
    - 수정
    - 참여
    - 나가기 (프로젝트에서 모두 나가면 프로젝트 삭제)
    - 권한 필터링 : 그 프로젝트에 속한 멤버만 접근 가능하도록함
 - 멤버 
    - 회원 가입, 
    - 로그인, 
    - 멤버 (이메일, 닉네임)으로 검색 
 - 파일 
    - 업로드,
    - 다운로드,
    - 프리뷰

사용한 기술
 - spring security, spring session,
 - Querydsl
 - minio
