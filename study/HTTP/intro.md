HTTP1.1 vs HTTP2

Hypertext Transfer Protocal

HTTP1.1은
 1. 1999년 출시
 2. 웹(WWW; World Wide Web)상에서 클라이언트와 웹서버간 통신을 위한 프로토콜
 3. 연결당 하나의 요청과 응답 처리 -> 동시전송 문제 및 다수의 리소스 처리에 속도와 성능 이슈
 4. 특정응답 지연 등등의 이슈
 5. 위와 같은 문제점을 해결하기 위해
  - 이미지 스프라이트
  - 도메인 샤딩
  - CSS/JavaScript 압축
  6. 텍스트 교환
    - 바이너리 데이터로 되어 있는 것도 아니고 단순 텍스트를 주고 받기 때문에 누군가 네트워크에서 신호를 가로채어 본다면 내용이 노출된다.

    ** 이런 보안상 문제를 해결해주는 프로토콜이 HTTPS...

HTTPS는
  1. 인터넷상에서 정보를 암호화하는 SSL(Secure Socket Layer)프로토콜을 이용하여 클라와 서버가 데이터를 주고 받는 통신 규약
  2. http 메시지(text)를 암호화 하는 것
  3. 공개키 암호화 방식
    - 암호화, 복호화시킬 수 있는 서로 다른 키 2개가 존재하는데 이 두 개의 키는 서로 1번 키로 암호화하면 반드시 2번키로만 복호화할 수 있고 2번 키로 암호화하면 반드시 1번키로만 복호화할 수 있는 룰이 있음
    - 그중 하나는 모두에게 공개하는 공개키(1번키)로 만들어서 공개키 저장소에 등록, 서버는 서버만 알수 있는 개인키(2번키)만 소유하고 있음 된다.
    - 보안상 의미보단 해당 서버로 부터 온 응답임을 확신할 수 있음


HTTP2는
  1. 성능뿐아니라 속도면에서도 월등
  2. Multiplexed Streams : 한 커넥션에 여러개의 메세지를 동시에 주고 받을 수 있음
    - 단일 TCP 연결을 통해 여러 데이터 요청을 병렬로 보낼 수 있음
    - 한 서버에서 비동기 적으로 웹 파일을 다운로드 할 수 있음
    - 최신 브라우저는 하나의 서버에 대한 TCP 연결을 제한함
    - 추가 왕복 시간 (RTT)이 줄어들어 최적화없이 웹 사이트를 더 빠르게 로드 할 수 있으며 도메인 샤딩이 필요하지 않는다.

    ** HTTP1.1은 여러개의 TCP 연결이 필요하며 브라우저에서는 한번에 최대 도메인당 6~8개 정도의 연결이 가능하도록 제한한다.
    --> 순차적으로 받으면 속도 당연 문제가 있겠지?
    --> 도메인 샤딩 : 정적파일 로딩 속도 개선하는 방법으로 여러개의 서브 도메인을 생성하여 정적파일을 병렬로 가져온다.
    --> 도메인 샤딩으로 웹 페이지 로딩 속도가 무조건 빨리지는 건 아님
    --> 각 하위 도메인에 DNS 조회함으로 많은 시간과 CPU 전력을 소모

  3. Stream Prioritization : 요청 리소스간 의존 관계를 설정
  4. Server Push : HTML문서상에 필요한 리소스를 클라이언트 요청없이 보내줄 수 있음
    - 클라이언트가 자원 X를 요청하고 자원 Y가 요청한 파일에서 참조되는 것을 서버가 알고 있으면
    - 서버는 클라이언트 요청을 기다리는 대신 X에서 Y를 밀어 선택할 수 있다.?
  5. Header Compression : Header정보를 HPACK 압축방식을 이용하여 압축전
  6. 이진 프로토콜
    - 텍스트 프로토콜에서 바이너리 프로토콜로 변환
    - HTTP1.x는 텍스트 명령을 처리
    - HTTP2는 2진 명령을 사용
    - 브라우저에서 네트워크를 통해 전송하기 전 바이너리로 변환
    * 장점 :
      - 데이터 분석시 오버헤드가 적다.
      - 오류가 발생하지 않는다.
      - 네트워크의 공간 절약, 효과적인 리소스 활용