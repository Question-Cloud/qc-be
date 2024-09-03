package com.eager.questioncloud.mail;

import lombok.Getter;

@Getter
public class ChangePasswordEmailVerificationTemplate extends EmailVerificationTemplate {
    String title = "[QuestionCloud] 비밀번호 변경 메일";
    String content;

    public ChangePasswordEmailVerificationTemplate(String token) {
        super();
        this.content = """
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
                    <h2>비밀번호 변경을 해주세요</h2>
                    <p>안녕하세요.<br>
                    아래 '비밀번호 변경' 버튼을 클릭하여 완료해 주세요.<br>
                    감사합니다.</p>
                    <a href="http://localhost:3000/change-password?token=%s" class="button">비밀번호 변경</a>
                    <p class="footer">만약 버튼이 정상적으로 클릭되지 않는다면, 아래 링크를 복사하여 접속해 주세요.<br>
                    <a href="http://localhost:3000/change-password?token=%s">http://localhost:3000/change-password?token=%s</a></p>
                </div>
            </body>
            </html>
            """.formatted(token, token, token);
    }
}
