# TODO
- 예외 처리에 대한 정리가 덜 되었음. 실제 운영 상에서 예외 발생에 대한 대응이 미흡함을 확인하였음. 이 부분에 대한 촘촘한 테스트코드 작성 필요.
- 발송 혹은 전달 된 메시지에 대해서 처리하는 방식이 두 가지임. 두 가지 모두 현재 상황에서 문제를 발생시킴
  - 전달 : 전달은 사실상 update에 다시 갱신하는 것과 같음. 이전의 update 하나와, 추가적인 chat을 발생시킴.
  - 답장 :  reply_to_message 등 대응 필요
    - {"ok":true,"result":[{"update_id":567841901,
    "message":{"message_id":2375,"from":{"id":39327045,"is_bot":false,"first_name":"\uc11d\uc9c4","language_code":"ko"},"chat":{"id":39327045,"first_name":"\uc11d\uc9c4","type":"private"},"date":1657031357,"reply_to_message":{"message_id":2372,"from":{"id":39327045,"is_bot":false,"first_name":"\uc11d\uc9c4","language_code":"ko"},"chat":{"id":39327045,"first_name":"\uc11d\uc9c4","type":"private"},"date":1657031087,"forward_from":{"id":1959903402,"is_bot":true,"first_name":"coffs_test","username":"coffs_dic_test_bot"},"forward_date":1656700053,"document":{"file_name":"sample.xlsx","mime_type":"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet","file_id":"BQACAgUAAxkBAAIJRGLESa_ZSHj1tJ2AyAwvFVYj-hDKAAL_BAACg56JVdF3guuN7A6tKQQ","file_unique_id":"AgAD_wQAAoOeiVU","file_size":26440},"caption":"\uc774\ud0c8\ub9ad\uba54\uc2dc\uc9c0!","caption_entities":[{"offset":0,"length":7,"type":"italic"}]},"text":"excel push"}}]} 
- send를 할 때 그것의 요청 주체가 무엇인지 확인되지 않음. 이 부분은 객체 그래프로 해야 할텐데, 어떤 식으로 할지 고민임. send를 보내는 주체는 무엇무엇이 될까?
- 전반적으로 동작은 잘 하는 것으로 보임. 일단 이 느낌으로 계속 개발하면 될 듯.
- file의 경우  File.createTempFile(?) 이 test에서는 정상 동작하는데 톰캣을 올리면 정상동작하지 않음. 결국 내부 드라이브를 만들고 그곳에 저장하도록 하였음. 임시 파일은 사용하지 못하는건가? 