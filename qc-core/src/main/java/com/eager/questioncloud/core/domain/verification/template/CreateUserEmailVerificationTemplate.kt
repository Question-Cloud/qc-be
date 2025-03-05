package com.eager.questioncloud.core.domain.verification.template

class CreateUserEmailVerificationTemplate(token: String) : EmailVerificationTemplate() {
    override var title: String? = "[QuestionCloud] 회원가입 인증 메일"
    override var content: String? = """
        <!DOCTYPE html>
        <html lang="ko">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <style>
                body {
                    font-family: Arial, sans-serif;
                    margin: 0;
                    padding: 20px;
                    background-color: #f4f4f4;
                }
                .container {
                    max-width: 600px;
                    margin: 0 auto;
                    background: #ffffff;
                    border-radius: 8px;
                    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
                    padding: 20px;
                }
                h1 {
                    color: #4CAF50;
                }
                p {
                    font-size: 16px;
                    line-height: 1.5;
                }
                .button {
                    display: inline-block;
                    padding: 10px 20px;
                    margin: 20px 0;
                    color: #ffffff;
                    background-color: #4CAF50;
                    text-decoration: none;
                    border-radius: 5px;
                }
                .footer {
                    font-size: 12px;
                    color: #777777;
                    margin-top: 20px;
                }
            </style>
        </head>
        <body>
            <div class="container">
                <h1>QuestionCloud</h1>
                <h2>메일인증 안내입니다.</h2>
                <p>안녕하세요.<br>
                QuestionCloud를 이용해 주셔서 진심으로 감사드립니다.<br>
                아래 '메일 인증' 버튼을 클릭하여 회원가입을 완료해 주세요.<br>
                감사합니다.</p>
                <a href="http://localhost:3000/email-verification?token=%s" class="button">메일 인증</a>
                <p class="footer">만약 버튼이 정상적으로 클릭되지 않는다면, 아래 링크를 복사하여 접속해 주세요.<br>
                <a href="http://localhost:3000/email-verification?token=%s">http://localhost:3000/email-verification?token=%s</a></p>
            </div>
        </body>
        </html>
        
        """.trimIndent().format(token, token, token)
}
