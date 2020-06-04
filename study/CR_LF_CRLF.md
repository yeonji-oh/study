# CR vs LF vs CR LF

## 이슈
- GIT 으로 동일한 파일을 여러명이 작업 할 때 머지 하면 해당파일이 전체 충돌이 나는 이슈
- 여러명의 작업자가 라인피드 환경설정이 다 달라서 생긴 충돌...

## 용어설명
- CR = Carriage Return
- LF = Line Feed
- CR 과 LF 는 줄바꿈을 의미하는 컨트롤 캐릭터

1. Carriage Return (MAC pre-OSX)
- \\r
- ASCII code 13 (0x0D)

2. Line Feed (Linux, MAC OSX)
- \\n
- ASCII code 10 (0x0A)

3. Carriage Return and Line Feed (Windows)
- \r\n
- ASCII code 13 and then ASCII code 10
If you see ASCII code in a strange format, they are merely the number 13 and 10 in a different radix/base, usually base 8 (octal) or base 16 (hexadecimal).

# 참고
- https://stackoverflow.com/questions/1552749/difference-between-cr-lf-lf-and-cr-line-break-types
