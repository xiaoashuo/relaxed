# Oauth2的基本使用

## 简介

参考: http://www.ballcat.cn/guide/security/oauth2.html#%E6%8E%88%E6%9D%83%E8%AE%B8%E5%8F%AF

这里只摘抄了部分文档，以便大家粗略了解 OAuth2 的 4 种授权类型。

更多内容可阅读 [OAuth2 RFC6749open in new window](https://datatracker.ietf.org/doc/html/rfc6749)，中文翻译参看 [RFC6749 中文open in new window](https://www.bookstack.cn/read/RFC6749.zh-cn/SUMMARY.md)。

OAuth 2.0 是一种授权协议。

在 OAuth 2.0 协议中，客户端在请求受保护的资源时，会通过一个 access token（一个代表特定的作用域、生命周期以及其他访问属性的字符串）来作为凭证，access token 由授权服务器在资源所有者认可的情况下颁发给第三方客户端。

先扔一张 OAuth 2.0 的 4 中授权许可的总结表格，防止下面文章太长不看：

![OAuth2](https://hccake-img.oss-cn-shanghai.aliyuncs.com/md-source/OAuth2.png)

## 角色

OAuth 中定义了 4 种角色：

- **资源所有者** `resource owner`：

能够授予对受保护资源的访问权的实体。 当资源所有者是人时，它被称为 end-user。

- **资源服务器** `reosource server`：

  存放受保护资源的服务器，能够通过 access token 来请求和响应这些受保护的资源。

- **客户端** `client`：

  请求受保护资源的的一方就可以被看作一个客户端。（这个客户端只是一个概念，具体实现可以是服务器，应用程序，或者 Html 网页 等等，一个资源服务器在请求另一个资源服务器的受保护资源时，其也被视为一个客户端）

- **授权服务器** `authorization server`：

  当客户端成功通过认证后，向其颁发 token 的服务器

## 协议流程

整体的协议流程大致可以抽象为下图所示，实际的执行流程，根据不同的授权方式，会各有不同。

```text
     +--------+                               +---------------+
     |        |--(A)- Authorization Request ->|   Resource    |
     |        |                               |     Owner     |
     |        |<-(B)-- Authorization Grant ---|               |
     |        |                               +---------------+
     |        |
     |        |                               +---------------+
     |        |--(C)-- Authorization Grant -->| Authorization |
     | Client |                               |     Server    |
     |        |<-(D)----- Access Token -------|               |
     |        |                               +---------------+
     |        |
     |        |                               +---------------+
     |        |--(E)----- Access Token ------>|    Resource   |
     |        |                               |     Server    |
     |        |<-(F)--- Protected Resource ---|               |
     +--------+                               +---------------+
```

- (A) 客户端向资源所有者请求授权。 授权请求可以直接向资源所有者发出(例如密码模式，资源所有者会直接将自己的用户名密码授予给客户端)，但是推荐客户端经由授权服务器作为中转向资源所有者发出(例如授权码模式)
- (B) 客户端收到授权许可，这是一个代表资源所有者的授权的凭据，使用本规范中定义的四种许可类型之一或者使用扩展许可类型表示。授权许可类型取决于客户端请求授权所使用的方法以及授权服务器支持的类型。
- (C) 客户端与授权服务器进行身份认证并出示授权许可来请求 access token 。
- (D) 授权服务器验证客户端以及授权许可，如果授权许可有效，则发出 access token 。
- (E) 客户端向资源服务器请求受保护的资源，并携带 access token 以进行身份验证。
- (F) 资源服务器验证 access token ，如果有效，则返回其请求的受保护资源。

## 授权许可

授权许可是一个代表资源所有者授权（访问受保护资源）的凭据，客户端用它来获取访问令牌。

OAuth 定义了四种许可类型——授权码、隐式许可、资源所有者密码凭据和客户端凭据——以及用于定义其他类型的可扩展性机制。

### 授权码 Authorization Code

grant_type：code

授权码许可类型中，客户端不会直接向资源所有者申请授权，而是通过授权服务中介处理的。整个流程基于重定向，要求客户端必须能够与资源所有者的用户代理（通常是 web 浏览器）进行交互并能够接收来自授权服务器的传入请求（通过重定向）。

```text
     +----------+
     | Resource |
     |   Owner  |
     |          |
     +----------+
          ^
          |
         (B)
     +----|-----+          Client Identifier      +---------------+
     |         -+----(A)-- & Redirection URI ---->|               |
     |  User-   |                                 | Authorization |
     |  Agent  -+----(B)-- User authenticates --->|     Server    |
     |          |                                 |               |
     |         -+----(C)-- Authorization Code ---<|               |
     +-|----|---+                                 +---------------+
       |    |                                         ^      v
      (A)  (C)                                        |      |
       |    |                                         |      |
       ^    v                                         |      |
     +---------+                                      |      |
     |         |>---(D)-- Authorization Code ---------'      |
     |  Client |          & Redirection URI                  |
     |         |                                             |
     |         |<---(E)----- Access Token -------------------'
     +---------+       (w/ Optional Refresh Token)
     
     						授权码流程图
```



注：说明步骤（A）、（B）和（C）的直线因为通过用户代理而被分为两部分。

- （A）客户端通过将资源所有者的用户代理定向到授权端点来启动流程。 客户端包括其客户端标识符、请求的范围、本地状态和重定向URI，授权服务器将在授予(或拒绝)访问后将用户代理发送回该重定向URI。
- （B）授权服务器(通过用户代理)对资源所有者进行身份验证，并确定资源所有者是授予还是拒绝客户端的访问请求。
- （C）假设资源所有者授予访问权，授权服务器使用先前提供的重定向URI(在请求中或在客户端注册期间提供)将用户代理重定向回客户端。 重定向URI的参数中包括一个授权代码和前面客户机提供的任何本地状态。
- （D）客户端通过包含上一步中收到的授权码从授权服务器的令牌端点请求 access token。当发起请求时，客户端与授权服务器进行身份验证。客户端包含用于获得授权码的重定向URI来用于验证。
- （E）授权服务器对客户端进行身份验证，验证授权代码，并确保接收的重定向URI与在步骤（C）中用于重定向（资源所有者的用户代理）到客户端的URI相匹配。如果通过，授权服务器响应返回 access token 与可选的 refresh token

#### 授权请求

客户端向授权端点发起请求时，其 URI 中的 QueryString，必须添加以下参数

| 参数          | 必传 | 描述                                                     |
| ------------- | ---- | -------------------------------------------------------- |
| response_type | 是   | 值必须是 "code"                                          |
| client_id     | 是   | 客户端标识                                               |
| redirect_uri  | 否   | 重定向地址，如果客户端在授权服务器中注册时已提供则可不传 |
| scope         | 否   | 请求访问的范围。                                         |
| state         | 否   | 推荐携带此值，用于防止跨站请求伪造                       |

请求示例：

```http
GET /authorize?response_type=code&client_id=s6BhdRkqt3&state=xyz&redirect_uri=https%3A%2F%2Fclient%2Eexample%2Ecom%2Fcb HTTP/1.1
Host: server.example.com
```

授权服务器验证该请求，确保所有需要的参数已提交且有效。如果请求是有效的，授权服务器对资源所有者进行身份验证并获得授权决定（通过询问资源所有者或通过经由其他方式确定批准）。

当确定决定后，授权服务器使用HTTP重定向响应向提供的客户端重定向URI定向用户代理，或者通过经由用户代理至该URI的其他可行方法。

#### 授权响应

如果资源所有者许可访问请求，授权服务器颁发授权码，通过向重定向URI的查询部分添加下列参数传递授权码至客户端：

| 参数  | 必传 | 描述                                                         |
| ----- | ---- | ------------------------------------------------------------ |
| code  | 是   | 授权码必须在颁发后很快过期以减小泄露风险。推荐的最长的授权码生命周期是10分钟。客户端不能使用授权码超过一次。如果一个授权码被使用一次以上，授权服务器必须拒绝该请求并应该撤销（如可能）先前发出的基于该授权码的所有令牌。授权码与客户端标识和重定向URI绑定。 |
| state | 否   | 当授权请求携带此参数时则必传，值原封不动回传                 |

例如，授权服务器通过发送以下HTTP响应重定向用户代理：

```http
HTTP/1.1 302 Found
Location: https://client.example.com/cb?code=SplxlOBeZQQYbYS6WxSbIA&state=xyz
```

客户端必须忽略无法识别的响应参数。 OAuth 未定义授权代码字符串的大小。 客户端应该避免对授权码的大小做出假设。 授权服务器应该记录它发出的任何值的大小。

#### 访问令牌请求

客户端发起向授权服务器的令牌端点发起一个 POST 请求，其 Content-type 必须为 “application/x-www-form-urlencoded”，并在其请求体中需要包含以下参数：

| 参数         | 必传 | 描述                                                         |
| ------------ | ---- | ------------------------------------------------------------ |
| grant_type   | 是   | 值必须是 “authorization_code”                                |
| code         | 是   | 值为上一步从授权服务器中收到的授权码                         |
| redirect_uri | 是   | 如果授权请求中携带了redirect_uri参数，则这里的值必须其相同   |
| client_id    | 是   | 如果客户端没有和授权服务器进行过 Client Credentials 的身份验证，则必须携带 |

如果客户端类型为机密或客户端颁发了客户端凭据(或分配了其他认证要求)，则客户端必须向授权服务器进行 Client Credentials 的身份验证。

例如：

```http
POST /token HTTP/1.1
Host: server.example.com
Authorization: Basic czZCaGRSa3F0MzpnWDFmQmF0M2JW
Content-Type: application/x-www-form-urlencoded

grant_type=authorization_code&code=SplxlOBeZQQYbYS6WxSbIA
     &redirect_uri=https%3A%2F%2Fclient%2Eexample%2Ecom%2Fcb
```

授权服务器必须：

- 要求机密客户端或任何被颁发了客户端凭据（或有其他身份验证要求）的客户端进行客户端身份验证，
- 若包括了客户端身份验证，验证客户端身份，
- 确保授权码颁发给了通过身份验证的机密客户端，或者如果客户端是公开的，确保代码颁发给了请求中的“client_id”，
- 验证授权码是有效的，并
- 确保给出了 “redirect_uri” 参数，若 “redirect_uri” 参数包含在初始授权请求中，确保它们的值是相同的。

#### 访问令牌响应

如果访问令牌请求有效且已授权，授权服务器将发出访问令牌和可选的刷新令牌。 如果请求客户端认证失败或无效，授权服务器将返回一个错误响应。

成功响应示例：

```http
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8
Cache-Control: no-store
Pragma: no-cache

{
    "access_token":"2YotnFZFEjr1zCsicMWpAA",
    "token_type":"example",
    "expires_in":3600,
    "refresh_token":"tGzv3JOkF0XG5Qx2TlKWIA",
    "example_parameter":"example_value"
}
```

### 隐式授权 Implicit

grant_type：implicit

隐式授权是为用如 JavaScript 等脚本语言在浏览器种实现的客户端而优化的一种简化的授权码流程。在隐式授权流程种，不再给客户端办法授权码，而是直接给客户端颁发一个 access token。

```text
     +----------+
     | Resource |
     |  Owner   |
     |          |
     +----------+
          ^
          |
         (B)
     +----|-----+          Client Identifier     +---------------+
     |         -+----(A)-- & Redirection URI --->|               |
     |  User-   |                                | Authorization |
     |  Agent  -|----(B)-- User authenticates -->|     Server    |
     |          |                                |               |
     |          |<---(C)--- Redirection URI ----<|               |
     |          |          with Access Token     +---------------+
     |          |            in Fragment
     |          |                                +---------------+
     |          |----(D)--- Redirection URI ---->|   Web-Hosted  |
     |          |          without Fragment      |     Client    |
     |          |                                |    Resource   |
     |     (F)  |<---(E)------- Script ---------<|               |
     |          |                                +---------------+
     +-|--------+
       |    |
      (A)  (G) Access Token
       |    |
       ^    v
     +---------+
     |         |
     |  Client |
     |         |
     +---------+
```

- (A) 客户端通过将资源所有者的用户代理定向到授权端点来启动流程。 客户端包括其客户端标识符、请求的范围、本地状态和重定向URI，授权服务器将在授予(或拒绝)访问后将用户代理发送回该重定向URI。
- (B) 授权服务器(通过用户代理)对资源所有者进行身份验证，并确定资源所有者是授予还是拒绝客户端的访问请求。
- (C) 假设资源所有者授予访问权，授权服务器使用先前提供的重定向URI(在请求中或在客户端注册期间提供)将用户代理重定向回客户端。 **重定向URI 的 Hash** 中将包含 access token。
- (D) 用户代理遵循重定向指令，向 web-hosted 的客户端资源发出请求（根据 [RFC2616]，URI 中的 Hash 部分不会携带在请求 URI 中携带）。用户代理将 Hash 中的参数取出并保存。
- (E) web-hosted 的客户端资源返回一个web页面(通常是一个带有嵌入式脚本的HTML文档)，该页面能够访问完整的重定向URI，包括用户代理保留的片段，并提取片段中包含的访问令牌(和其他参数)。
- (F) 用户代理在本地执行由 web-hosted 的客户端资源提供的脚本，从而提取访问令牌。
- (G) 用户代理将访问令牌传递给客户端。

> (D) (E) 为非必选步骤，主要用于当用户代理不支持在 response header 的 Location 属性中包含 Hash 片段时，通过返回一个内嵌 javascript 的 html 页面，页面内引导用户点击按钮跳转向 redirect_url

#### 授权请求

客户端向授权端点发起请求时，其 URI 中的 QueryString，必须添加以下参数

| 参数          | 必传 | 描述                                                     |
| ------------- | ---- | -------------------------------------------------------- |
| response_type | 是   | 值必须是 "token"                                         |
| client_id     | 是   | 客户端标识                                               |
| redirect_uri  | 否   | 重定向地址，如果客户端在授权服务器中注册时已提供则可不传 |
| scope         | 否   | 请求访问的范围。                                         |
| state         | 否   | 推荐携带此值，用于防止跨站请求伪造                       |

请求示例：

```http
GET /authorize?response_type=token&client_id=s6BhdRkqt3&state=xyz&redirect_uri=https%3A%2F%2Fclient%2Eexample%2Ecom%2Fcb HTTP/1.1
Host: server.example.com
```

授权服务器验证该请求，确保所有需要的参数已提交且有效。授权服务器必须验证它将访问令牌重定向到的重定向URI与客户端注册的重定向URI匹配。

如果请求是有效的，授权服务器对资源所有者进行身份验证并获得授权决定（通过询问资源所有者或通过经由其他方式确定批准）。

当确定决定后，授权服务器使用HTTP重定向响应向提供的客户端重定向URI定向用户代理，或者通过经由用户代理至该URI的其他可行方法。

#### 授权响应

如果资源所有者许可访问请求，授权服务器直接颁发访问令牌，通过向重定向URI的 Hash 部分添加下列参数传递 access token 至客户端：

| 参数         | 必传 | 描述                                                         |
| ------------ | ---- | ------------------------------------------------------------ |
| access_token | 是   | 授权服务器颁发的访问令牌。                                   |
| token_type   | 是   | 颁发的令牌的类型，其值是大小写不敏感的。（一般是 Bearer）    |
| expires_in   | 否   | 推荐的。以秒为单位的访问令牌生命周期。例如，值“3600”表示访问令牌将在从生成响应时的1小时后到期。如果省略，则授权服务器应该通过其他方式提供过期时间，或者记录默认值。 |
| scope        | 否   | 若与客户端请求的 scope 范围相同则可以不传，否则必需返回此值。 |
| state        | 否   | 当授权请求携带此参数时则必传，值原封不动回传                 |

例如，授权服务器通过发送以下HTTP响应重定向用户代理：

```http
HTTP/1.1 302 Found
Location: http://example.com/cb#access_token=2YotnFZFEjr1zCsicMWpAA&state=xyz&token_type=example&expires_in=3600
```

开发人员应注意，一些用户代理不支持在HTTP“Location”HTTP响应标头字段中包含片段组成部分。这些客户端需要使用除了3xx重定向响应以外的其他方法来重定向客户端——-例如，返回一个HTML页面，其中包含一个具有链接到重定向URI的动作的“继续”按钮。

客户端必须忽略无法识别的响应参数。 OAuth 未定义授权代码字符串的大小。 客户端应该避免对授权码的大小做出假设。 授权服务器应该记录它发出的任何值的大小。

### 资源所有者密码凭证 Resource Owner Password Credentials

grant_type：password

资源所有者密码凭据许可类型适合于资源所有者与客户端具有信任关系的情况，如设备操作系统或高级特权应用。当启用这种许可类型时授权服务器应该特别关照且只有当其他流程都不可用时才可以。

这种许可类型适合于能够获得资源所有者凭据（用户名和密码，通常使用交互的形式）的客户端。通过转换已存储的凭据至访问令牌，它也用于迁移现存的使用如HTTP基本或摘要身份验证的直接身份验证方案的客户端至OAuth。

```text
     +----------+
     | Resource |
     |  Owner   |
     |          |
     +----------+
          v
          |    Resource Owner
         (A) Password Credentials
          |
          v
     +---------+                                  +---------------+
     |         |>--(B)---- Resource Owner ------->|               |
     |         |         Password Credentials     | Authorization |
     | Client  |                                  |     Server    |
     |         |<--(C)---- Access Token ---------<|               |
     |         |    (w/ Optional Refresh Token)   |               |
     +---------+                                  +---------------+
```

- (A) 资源所有者提供给客户端它的用户名和密码。
- (B) 通过包含从资源所有者处接收到的凭据，客户端从授权服务器的令牌端点请求访问令牌。当发起请求时，客户端与授权服务器进行身份验证。
- (C) 授权服务器对客户端进行身份验证，验证资源所有者的凭证，如果有效，颁发访问令牌。

#### 授权请求和响应

客户端获取资源所有者凭据 (用户名/密码) 的方法超出了本规范的范围。 一旦获得了访问令牌，客户端必须丢弃凭据。

#### 访问令牌请求

客户端向授权服务器的令牌端点发起一个 POST 请求，其 Content-type 必须为 “application/x-www-form-urlencoded”，并在其请求体中需要包含以下参数：

| 参数       | 必传 | 描述                 |
| ---------- | ---- | -------------------- |
| grant_type | 是   | 值必须是 “password”  |
| username   | 是   | 资源所有者的用户名。 |
| password   | 是   | 资源所有者的密码。   |
| scope      | 是   | 请求访问的范围       |

如果客户端类型是机密的或客户端被颁发了客户端凭据，则客户端必须要与授权服务器进行身份验证（request header 中携带 Authorization，值为 Base64(clientId:clientSecret) ）。

例如：

```http
POST /token HTTP/1.1
Host: server.example.com
Authorization: Basic czZCaGRSa3F0MzpnWDFmQmF0M2JW
Content-Type: application/x-www-form-urlencoded
grant_type=password&username=johndoe&password=A3ddj3w
```

授权服务器必须：

- 要求机密客户端或任何被颁发了客户端凭据（或有其他身份验证要求）的客户端进行客户端身份验证，
- 若包括了客户端身份验证，验证客户端身份
- 使用它现有的密码验证算法验证资源所有者的密码凭据。

由于这种访问令牌请求使用了资源所有者的密码，授权服务器必须保护端点防止暴力攻击（例如，使用速率限制或生成警报）。

#### 访问令牌响应

如果访问令牌请求有效且已授权，授权服务器将发出访问令牌和可选的刷新令牌。 如果请求客户端认证失败或无效，授权服务器将返回一个错误响应。 一个成功响应的示例如下:

```http
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8
Cache-Control: no-store
Pragma: no-cache

{
  "access_token":"2YotnFZFEjr1zCsicMWpAA",
  "token_type":"example",
  "expires_in":3600,
  "refresh_token":"tGzv3JOkF0XG5Qx2TlKWIA",
  "example_parameter":"example_value"
}
```

### 客户端凭证 Client Credentials

grant_type: client_credentials

当客户端请求访问它所控制的，或者事先与授权服务器协商（所采用的方法超出了本规范的范围）的其他资源所有者的受保护资源，客户端可以只使用它的客户端凭据（或者其他受支持的身份验证方法）请求访问令牌。

客户端凭据许可类型必须只能由机密客户端使用。

```text
     +---------+                                  +---------------+
     |         |                                  |               |
     |         |>--(A)- Client Authentication --->| Authorization |
     | Client  |                                  |     Server    |
     |         |<--(B)---- Access Token ---------<|               |
     |         |                                  |               |
     +---------+                                  +---------------+
```

- （A）客户端与授权服务器进行身份验证并向令牌端点请求访问令牌。
- （B）授权服务器对客户端进行身份验证，如果有效，颁发访问令牌。

#### 授权请求和响应

由于客户端身份验证被用作授权许可，所以不需要其他授权请求。

#### 访问令牌请求

客户端向授权服务器的令牌端点发起一个 POST 请求，其 Content-type 必须为 “application/x-www-form-urlencoded”，并在其请求体中需要包含以下参数：

| 参数       | 必传 | 描述                          |
| ---------- | ---- | ----------------------------- |
| grant_type | 是   | 值必须是 “client_credentials” |
| scope      | 是   | 请求访问的范围                |

一个请求示例如下：

```http
POST /token HTTP/1.1
Host: server.example.com
Authorization: Basic czZCaGRSa3F0MzpnWDFmQmF0M2JW
Content-Type: application/x-www-form-urlencoded

grant_type=client_credentials
```

授权服务器必须验证客户端身份。

#### 访问令牌响应

如果访问令牌请求有效且已授权，授权服务器将发出访问令牌，但并不包含刷新令牌。 如果请求客户端认证失败或无效，授权服务器将返回一个错误响应。

一个成功响应的示例如下:

```http
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8
Cache-Control: no-store
Pragma: no-cache

{
    "access_token":"2YotnFZFEjr1zCsicMWpAA",
    "token_type":"example",
    "expires_in":3600,
    "example_parameter":"example_value"
}
```

## 一、授权服务器

### 1.依赖引入

```xml
    <dependency>
            <groupId>com.lovecyy</groupId>
            <artifactId>relaxed-auth</artifactId>
            <version>${version}</version>
    </dependency>
```

### 2.配置客户端信息

> 默认是jdbc形式,参见`oauth_client_details`，演示使用内存模式

+ **InmemoryOauth2ClientConfigurer**

```java
@RequiredArgsConstructor
public class InmemoryOauth2ClientConfigurer implements OAuth2ClientConfigurer {

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory().withClient("admin")// 配置client_id
				.secret(PasswordUtils.encode("admin123456"))// 配置client_secret
				.accessTokenValiditySeconds(3600)// 配置访问token的有效期
				.refreshTokenValiditySeconds(864000)// 配置刷新token的有效期
				.redirectUris("http://www.baidu.com")// 配置redirect_uri，用于授权成功后跳转
				.scopes("server")// 配置申请的权限范围
				.authorizedGrantTypes("authorization_code", "password", "refresh_token")
				.autoApprove(true)// 配置grant_type，表示授权类型
				.resourceIds("test-server", "tes").and().withClient("resource")// 配置client_id
				.secret(PasswordUtils.encode("resource"))// 配置client_secret
				.accessTokenValiditySeconds(3600)// 配置访问token的有效期
				.refreshTokenValiditySeconds(864000)// 配置刷新token的有效期
				.redirectUris("http://www.baidu.com")// 配置redirect_uri，用于授权成功后跳转
				.scopes("server")// 配置申请的权限范围
				.authorizedGrantTypes("authorization_code", "password", "refresh_token")
				.autoApprove(true)// 配置grant_type，表示授权类型
		;
	}

}

```

+ **客户端Bean注册**

```java
	
@Bean
	public OAuth2ClientConfigurer oAuth2ClientConfigurer() {
		return new InmemoryOauth2ClientConfigurer();
	}
```

### 3.配置令牌存储

> 建议使用redis，jdbc或jwt形式，默认采用内存模式

```java
	/**
	 * 令牌存储
	 * @return
	 */
	@Bean
	public TokenStore tokenStore() {
		return new InMemoryTokenStore();
	}
```

### 4.配置Token增强(可选)

```java
	/**
	 * 配置token增强 可以附加扩展信息
	 */
	@Bean
	public ExtendTokenEnhancer extendTokenEnhancer() {
		return new ExtendTokenEnhancer();
	}


public class ExtendTokenEnhancer implements TokenEnhancer {

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
		Map<String, Object> info = new HashMap<>();
		info.put("enhance", "enhance info");
		((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(info);
		return oAuth2AccessToken;
	}

}



```

### 5.配置用户信息服务

> 继承UserDetailsService 

```java
@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService  {

	private List<User> userList;

	@PostConstruct
	public void initData() {
		String password = PasswordUtils.encode("123456");
		userList = new ArrayList<>();
		userList.add(new User("macro", password, AuthorityUtils.commaSeparatedStringToAuthorityList("admin")));
		userList.add(new User("andy", password, AuthorityUtils.commaSeparatedStringToAuthorityList("client")));
		userList.add(new User("mark", password, AuthorityUtils.commaSeparatedStringToAuthorityList("client")));
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<User> findUserList = userList.stream().filter(user -> user.getUsername().equals(username))
				.collect(Collectors.toList());
		if (!CollectionUtils.isEmpty(findUserList)) {
			User user = findUserList.get(0);
			User user1 = ObjectUtil.cloneByStream(user);
			;
			return user1;
		}
		else {
			throw new UsernameNotFoundException("用户名或密码错误");
		}
	}
```



### 6.开启授权服务器

```java
@EnableOauth2AuthorizationServer
@SpringBootApplication
public class AuthDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthDemoApplication.class, args);
	}

}

```

## 二、资源服务器

### 1.依赖引入

```xml
   <dependency>
            <groupId>com.lovecyy</groupId>
            <artifactId>relaxed-resource</artifactId>
            <version>${version}</version>
  </dependency>
```

### 2.application.yml

> 若授权服务器和资源服务器在同一项目,则只需要配置`extension`

```yml
#指定授权服务器地址
auth-server-address: localhost:9401
security:
  oauth2:
    client:
      client-id: admin
      client-secret: admin123456
      access-token-uri: http://${auth-server-address}/oauth/token
      user-authorization-uri: http://${auth-server-address}/oauth/authorize
    resource:
      #自省token地址
      token-info-uri: http://${auth-server-address}/oauth/check_token
    extension:
      #资源服务标识
      resourceId: order
      #忽略鉴权url
      ignoreUrls:
        - /user/**
```

### 3.开启资源服务器

```java
@EnableOauth2ResourceServer
@SpringBootApplication
public class AuthDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthDemoApplication.class, args);
	}

}
```

## 三、使用方式

> 访问Token请求，若请求参数包含client_id，则走`ClientCredentialsTokenEndpointFilter`   否则走`BasicAuthenticationFilter`

### 1.授权码模式

> grant_type=authorization_code

- 启动授权服务器；

- 在浏览器访问该地址进行登录授权：http://localhost:9401/oauth/authorize?response_type=code&client_id=admin&redirect_uri=http://www.baidu.com&scope=all&state=normal



  ![](https://cdn.jsdelivr.net/gh/xiaoashuo/pics/oauth2/授权码登录.png)

- 输入账号密码进行登录操作：

  ![](https://cdn.jsdelivr.net/gh/xiaoashuo/pics/oauth2/确认授权页.png)



- 之后会浏览器会带着授权码跳转到我们指定的路径：

```bash
https://www.baidu.com/?code=eTsADY&state=normal
```

- 使用授权码请求该地址获取访问令牌：http://localhost:9401/oauth/token
- 使用Basic认证通过client_id和client_secret构造一个Authorization头信息；

   ![](https://cdn.jsdelivr.net/gh/xiaoashuo/pics/oauth2/Basic认证.png)

- 在body中添加以下参数信息，通过POST请求获取访问令牌；

![](https://cdn.jsdelivr.net/gh/xiaoashuo/pics/oauth2/授权码模式.png)

### 2.密码模式

>grant_type=password

- 使用密码请求该地址获取访问令牌：http://localhost:9401/oauth/token

- 使用Basic认证通过client_id和client_secret构造一个Authorization头信息，

  ![](https://cdn.jsdelivr.net/gh/xiaoashuo/pics/oauth2/Basic认证.png)

  若客户端请求没有Basic Auth 则直接在请求头输入如下参数

  ```java
  Authorization: Basic dWk6dWk=
  dWk6dWk计算方式如下
  (clientId:clientSecret)=>byte[] key=>new String(Base64.encodeBase64(key))
  ```

- 在body中添加以下参数信息，通过POST请求获取访问令牌；

   ![](https://cdn.jsdelivr.net/gh/xiaoashuo/pics/oauth2/密码模式.png)

+ 在请求头中添加访问令牌，访问需要登录认证的接口进行测试，发现已经可以成功访问：http://localhost:9401/user/getCurrentUser

   ![](https://cdn.jsdelivr.net/gh/xiaoashuo/pics/oauth2/访问接口.png)



### 3.Implicit（简化模式）

> grant_type=implicit

+ 直接浏览器发起请求 http://localhost:9401/oauth/authorize?response_type=token&client_id=admin&redirect_uri=http://www.baidu.com&scope=server&state=normal

+ 会在重定向url#hash带上accesss_token

  ![](https://cdn.jsdelivr.net/gh/xiaoashuo/pics/oauth2/简化模式.png)

### 4.客户端凭证

> grant_type=client_credentials

+ 使用客户端凭证请求该地址获取访问令牌：http://localhost:9401/oauth/token

+ 使用Basic认证通过client_id和client_secret构造一个Authorization头信息，

![](https://cdn.jsdelivr.net/gh/xiaoashuo/pics/oauth2/Basic认证.png)

+ 在body中添加以下参数信息，通过POST请求获取访问令牌；

  ![](https://cdn.jsdelivr.net/gh/xiaoashuo/pics/oauth2/客户端模式.png)

### 5.刷新Token

> grant_type=refresh_token

- 使用客户端凭证请求该地址获取访问令牌：http://localhost:9401/oauth/token

- 使用Basic认证通过client_id和client_secret构造一个Authorization头信息，

  ![](https://cdn.jsdelivr.net/gh/xiaoashuo/pics/oauth2/Basic认证.png)

+ 在body中添加以下参数信息，通过POST请求获取访问令牌；

  ![](https://cdn.jsdelivr.net/gh/xiaoashuo/pics/oauth2/20220726164212.png)


# 附:

## 一、附带sql

### 1.oauth_client_details

授权码 主要操作`oauth_client_details`表的类是`JdbcClientDetailsService.java`

```sql
DROP TABLE IF EXISTS `oauth_client_details`;

CREATE TABLE `oauth_client_details` (
  `client_id` varchar(255) NOT NULL COMMENT '客户端标识',
  `resource_ids` varchar(255) DEFAULT NULL COMMENT '接入资源列表',
  `client_secret` varchar(255) DEFAULT NULL COMMENT '客户端秘钥',
  `scope` varchar(255) DEFAULT NULL, 
  `authorized_grant_types` varchar(255) DEFAULT NULL,
  `web_server_redirect_uri` varchar(255) DEFAULT NULL,
  `authorities` varchar(255) DEFAULT NULL,
  `access_token_validity` int(11) DEFAULT NULL,
  `refresh_token_validity` int(11) DEFAULT NULL,
  `additional_information` longtext,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `archived` tinyint(4) DEFAULT NULL,
  `trusted` tinyint(4) DEFAULT NULL,
  `autoapprove` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`client_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='接入客户端信息';

```



| 字段名                  | 类型      | 描述                                                         |
| ----------------------- | --------- | ------------------------------------------------------------ |
| client_id               | varchar   | 主键,必须唯一,不能为空. 用于唯一标识每一个客户端(client);    |
| resource_ids            | varchar   | 客户端所能访问的资源id集合,多个资源时用逗号(,),如果没设置，就是对所有的Resource Server都有访问权限。 |
| client_secret           | varchar   | 用于指定客户端(client)的访问密匙                             |
| scope                   | varchar   | 指定客户端申请的权限范围,可选值包括read,write,trust,all(可自定义）;若有多个权限范围用逗号(,)分隔,@EnableGlobalMethodSecurity(prePostEnabled = true)启用方法级权限控制 |
| authorized_grant_types  | varchar   | 然后在方法上注解标识@PreAuthorize("#oauth2.hasScope('read')")就是5种授权模式：authorization_code,password,refresh_token,implicit,client_credentials, 若支持多个grant_type用逗号(,)分隔, |
| web_server_redirect_uri | varchar   | 重定向地址，在oauth中会校验                                  |
| authorities             | varchar   | 指定客户端所拥有的权限值,可选, 若有多个权限值,用逗号(,)分隔。若授权模式中不需要账户密码的建议设；若授权模式需要账户密码的，可以不设立 |
| access_token_validity   | int       | 设定客户端的access_token的有效时间值(单位:秒) 【默认12小时】 |
| refresh_token_validity  | int       | 设定客户端的refresh_token的有效时间值(单位:秒）【默认30天】  |
| additional_information  | longtext  | 可以额外附带的信息，若赋值，则需要json规范                   |
| create_time             | timestamp | 数据的创建时间,精确到秒,由数据库在插入数据时取当前系统时间自动生成(扩展字段) |
| archived                | tinyint   | 标识是否存档，默认为0                                        |
| trusted                 | tinyint   | 标识是否受信任，默认0（0-不信任，1-信任）                    |
| autoapprove             | varchar   | 设置用户是否自动Approval操作，通常只在authorization_code 模式有效 |

### 2.oauth_code

支持授权码获取accessToken

```sql
CREATE TABLE IF NOT EXISTS `oauth_code` (
  `code` VARCHAR(256) NULL DEFAULT NULL,
  `authentication` BLOB NULL DEFAULT NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

```

### 3.oauth_access_token

对`oauth_client_token`表的主要操作在`JdbcClientTokenServices.java`，实际上未使用到

```sql
CREATE TABLE IF NOT EXISTS `oauth_access_token` (
  `token_id` VARCHAR(256) NULL DEFAULT NULL,
  `token` BLOB NULL DEFAULT NULL,
  `authentication_id` VARCHAR(128) NOT NULL,
  `user_name` VARCHAR(256) NULL DEFAULT NULL,
  `client_id` VARCHAR(256) NULL DEFAULT NULL,
  `authentication` BLOB NULL DEFAULT NULL,
  `refresh_token` VARCHAR(256) NULL DEFAULT NULL,
  PRIMARY KEY (`authentication_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

```

| 字段名            | 类型    | 描述                                                         |
| ----------------- | ------- | ------------------------------------------------------------ |
| token_id          | varchar | 从服务器端获取到的access_token的值.                          |
| token             | BLOB    | 这是一个二进制的字段, 存储的数据OAuth2AccessToken.java对象序列化后的二进制数据. |
| authentication_id | varchar | 该字段具有唯一性, 是根据当前的username(如果有),client_id与scope通过MD5加密生成的. 具体实现请参考DefaultClientKeyGenerator.java类. |
| user_name         | varchar | 登录时的用户名                                               |
| client_id         | varchar | 主键,必须唯一,不能为空. 用于唯一标识每一个客户端(client);    |
| authentication    | varchar | 存储将OAuth2Authentication.java对象序列化后的二进制数据.     |
| refresh_token     | varchar | 该字段的值是将refresh_token的值通过MD5加密后存储的.          |

### 4.oauth_refresh_token

```sql
CREATE TABLE IF NOT EXISTS `oauth_refresh_token` (
  `token_id` VARCHAR(256) NULL DEFAULT NULL,
  `token` BLOB NULL DEFAULT NULL,
  `authentication` BLOB NULL DEFAULT NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

```

| 字段名         | 类型    | 描述                                                         |
| -------------- | ------- | ------------------------------------------------------------ |
| token_id       | varchar | 从服务器端获取到的access_token的值.                          |
| token          | BLOB    | 这是一个二进制的字段, 存储的数据OAuth2AccessToken.java对象序列化后的二进制数据. |
| authentication | varchar | 存储将OAuth2Authentication.java对象序列化后的二进制数据.     |

## 二、扩展授权服务器

### 内置扩展器

#### 实现扩展用户信息服务

```java
@RequiredArgsConstructor
@Service
public class UserService implements ExtendUserDetailsService {

	private List<User> userList;

	@PostConstruct
	public void initData() {
		String password = PasswordUtils.encode("123456");
		userList = new ArrayList<>();
		userList.add(new User("macro", password, AuthorityUtils.commaSeparatedStringToAuthorityList("admin")));
		userList.add(new User("andy", password, AuthorityUtils.commaSeparatedStringToAuthorityList("client")));
		userList.add(new User("mark", password, AuthorityUtils.commaSeparatedStringToAuthorityList("client")));
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<User> findUserList = userList.stream().filter(user -> user.getUsername().equals(username))
				.collect(Collectors.toList());
		if (!CollectionUtils.isEmpty(findUserList)) {
			User user = findUserList.get(0);
			User user1 = ObjectUtil.cloneByStream(user);
			;
			return user1;
		}
		else {
			throw new UsernameNotFoundException("用户名或密码错误");
		}
	}

	@Override
	public UserDetails loginByMobile(String mobile) {
		String password = PasswordUtils.encode("123456");
		return new User("macro", password, AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
	}

}
```

#### 验证码登录

> 请求授权类型 grant_type = captcha 

```java
@Component
public class CustomCaptchaValidator implements CaptchaValidator {

	@Override
	public void validate(Map<String, String> parameters) {
		// 取出验证码
		String validateCode = parameters.get("code");
	
		Assert.isTrue(StrUtil.isNotBlank(validateCode), "验证码不能为空");
    
		String validateCodeKey = "CAPTCHA:" + uuid;
		// 从缓存取出正确的验证码和用户输入的验证码比对
		String correctValidateCode = CACHE.get(validateCodeKey);
		if (StrUtil.isEmpty(correctValidateCode)) {
			throw new InvalidGrantException("验证码已过期");
		}
		if (!validateCode.equals(correctValidateCode)) {
			throw new InvalidGrantException("您输入的验证码不正确");
		}
		// 验证码验证通过，删除 Redis 的验证码
		CACHE.remove(validateCodeKey);
		// 移除后续无用参数
		parameters.remove("code");
	}

}
```

#### 手机号登录

> 请求授权类型 grant_type = sms_code

```java
@Component
public class CustomSmsCodeValidator implements SmsCodeValidator {

	@Override
	public void authenticate(String mobile, String code) {
		if (!code.equals("666666")) { // 666666 是后门，因为短信收费，正式环境删除这个if分支
			// 短信验证码key前缀
			String codeKey = "SMS_CODE:" + mobile;
			// 根据验证码key获取验证码
			String correctCode = "";
			// 验证码比对
			if (StrUtil.isBlank(correctCode) || !code.equals(correctCode)) {
				throw new InvalidGrantException("验证码不正确");
			}
			// 比对成功删除缓存的验证码
			// ...del
		}
	}

}

```

### 拓展扩展器

#### 1.创建认证Token

```java
public class CustomAuthenticationToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = 550L;

	private final Object principal;

	private Object credentials;

	public CustomAuthenticationToken(Object principal, Object credentials) {
		super((Collection) null);
		this.principal = principal;
		this.credentials = credentials;
		this.setAuthenticated(false);
	}

	public CustomAuthenticationToken(Object principal, Object credentials,
			Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = principal;
		this.credentials = credentials;
		super.setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		return this.credentials;
	}

	@Override
	public Object getPrincipal() {
		return this.principal;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		Assert.isTrue(!isAuthenticated,
				"Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
		super.setAuthenticated(false);
	}

	@Override
	public void eraseCredentials() {
		super.eraseCredentials();
		this.credentials = null;
	}

}

```

#### 2.创建令牌授予者

```java
public class CustomTokenGranter extends AbstractTokenGranter {

	/**
	 * 声明授权者 CustomTokenGranter 支持授权模式 custom_code 根据接口传值 grant_type = custom_code 的值匹配到此授权者
	 * 匹配逻辑详见下面的两个方法
	 *
	 * @see CompositeTokenGranter#grant(String, TokenRequest)
	 * @see AbstractTokenGranter#grant(String, TokenRequest)
	 */
	private static final String GRANT_TYPE = "custom_code";

	private final AuthenticationManager authenticationManager;

	public CustomTokenGranter(AuthorizationServerTokenServices tokenServices,
			ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory,
			AuthenticationManager authenticationManager) {
		super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
		this.authenticationManager = authenticationManager;
	}

	@Override
	protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {

		Map<String, String> parameters = new LinkedHashMap(tokenRequest.getRequestParameters());

		String mobile = parameters.get("mobile"); // 手机号
		String code = parameters.get("code"); // 短信验证码

		parameters.remove("code");

		Authentication userAuth = new CustomAuthenticationToken(mobile, code);
		((AbstractAuthenticationToken) userAuth).setDetails(parameters);

		try {
			userAuth = this.authenticationManager.authenticate(userAuth);
		}
		catch (AccountStatusException exception) {
			throw new InvalidGrantException(exception.getMessage());
		}
		catch (BadCredentialsException exception) {
			throw new InvalidGrantException(exception.getMessage());
		}

		if (userAuth != null && userAuth.isAuthenticated()) {
			OAuth2Request storedOAuth2Request = this.getRequestFactory().createOAuth2Request(client, tokenRequest);
			return new OAuth2Authentication(storedOAuth2Request, userAuth);
		}
		else {
			throw new InvalidGrantException("Could not authenticate user: " + mobile);
		}
	}

}
```



#### 3.创建认证提供者

```java
/**
  * 自定义认证逻辑
  *
  */
@Data
public class CustomAuthenticationProvider implements AuthenticationProvider {

 

	private final UserDetailsService userDetailsService;
    
    
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		CustomAuthenticationToken authenticationToken = (CustomAuthenticationToken) authentication;
		String mobile = (String) authenticationToken.getPrincipal();
		String code = (String) authenticationToken.getCredentials();
 //自定义提取信息方式
UserDetails userDetails = ((ExtendUserDetailsService) userDetailsService).loginByMobile(mobile);
		CustomAuthenticationToken result = new CustomAuthenticationToken(userDetails, authentication.getCredentials(),
				new HashSet<>());
		result.setDetails(authentication.getDetails());
		return result;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return CustomAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
```

4.注册认证提供者

```
	@Bean
	public CustomAuthenticationProvider CustomAuthenticationProvider(UserDetailsService userDetailsService) {
		CustomAuthenticationProvider provider = new CustomAuthenticationProvider(userDetailsService);
		return provider;
	}
```

#### 5.注册自定义令牌授予者

```java
	@Bean
	@ConditionalOnMissingBean
	public TokenGrantBuilder tokenGrantBuilder(AuthenticationManager authenticationManager,
			@Autowired(required = false) CaptchaValidator captchaValidator) {

		// 添加验证码授权模式授权者
		TokenGranterProvider captchaTokenGranter = endpoints -> new CaptchaTokenGranter(endpoints.getTokenServices(),
				endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory(), authenticationManager,
				captchaValidator);
        
CustomTokenGranter customTokenGranter = endpoints -> new CustomTokenGranter(endpoints.getTokenServices(),
				endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory(), authenticationManager);
		List<TokenGranterProvider> tokenGranterProviderList = new ArrayList<>();
		tokenGranterProviderList.add(captchaTokenGranter);
       tokenGranterProviderList.add(customTokenGranter);
		TokenGrantBuilder tokenGrantBuilder = new TokenGrantBuilder(authenticationManager, tokenGranterProviderList);
		return tokenGrantBuilder;
	}
```

## 三、退出端点构造

### 1.JWT形式

JWT最大的一个优势在于它是**无状态**的，自身包含了认证鉴权所需要的所有信息，服务器端无需对其存储，从而给服务器减少了存储开销。

但是无状态引出的问题也是可想而知的，它无法作废未过期的JWT。

首先明确一点JWT失效的唯一途径就是等过期，就是说不借助外力的情况下，无法达到某些场景下需要主动使JWT失效的目的。而外力则是在服务器端存储着JWT的状态，在请求资源时添加判断逻辑，这与JWT特性无状态是相互矛盾的存在。但是，你要知道如果你选择走上了JWT这条路，那就没得选了。

以下就JWT在某些场景需要失效的简单方案整理如下：

#### **1. 白名单方式**

认证通过时，把JWT缓存到Redis，注销时，从缓存移除JWT。请求资源添加判断JWT在缓存中是否存在，不存在拒绝访问。这种方式和cookie/session机制中的会话失效删除session基本一致。

#### **2. 黑名单方式**

注销登录时，缓存JWT至Redis，且缓存有效时间设置为JWT的有效期，请求资源时判断是否存在缓存的黑名单中，存在则拒绝访问。

白名单和黑名单的实现逻辑差不多，黑名单不需每次登录都将JWT缓存，仅仅在某些特殊场景下需要缓存JWT，给服务器带来的压力要远远小于白名单的方式。

##### 实现方案：

1.调用退出登录接口  `/oauth/logout`

2.将JWT缓存到Redis的黑名单，可以将JWT载体中的jti作为唯一标识

3.判定请求头的JWT是否在黑名单内做对应的处理

JWT解析的结构如下：

![](https://cdn.jsdelivr.net/gh/xiaoashuo/pics/oauth2/20220726172350.png)

既然有这么个字段能作为JWT的唯一标识，从JWT解析出jti之后将其存储到黑名单中作为判别依据，

相较于存储完整的JWT字符串减少了存储开销。

另外我们只需保证JWT在其有效期内用户登出后失效就可以了，

JWT有效期过了黑名单也就没有存在的必要，所以我们这里还需要设置黑名单的过期时间，不然黑名单的数量会无休止的越来越多，这是我们不想看到的。

+ **AuthController.java**

```java
@Api(tags = "认证中心")
@RestController
@RequestMapping("/oauth")
@AllArgsConstructor
public class AuthController {

    private RedisTemplate redisTemplate;

    @DeleteMapping("/logout")
    public R logout(HttpServletRequest request) {
        String payload = request.getHeader(AuthConstants.JWT_PAYLOAD_KEY);
        JSONObject jsonObject = JSONUtil.parseObj(payload);

        String jti = jsonObject.getStr("jti"); // JWT唯一标识
        long exp = jsonObject.getLong("exp"); // JWT过期时间戳(单位:秒)

        long currentTimeSeconds = System.currentTimeMillis() / 1000;

        if (exp < currentTimeSeconds) { // token已过期
            return R.ok(ResultCode.INVALID_TOKEN_OR_EXPIRED);
        }
        redisTemplate.opsForValue().set(AuthConstants.TOKEN_BLACKLIST_PREFIX + jti, null, (exp - currentTimeSeconds), TimeUnit.SECONDS);
        return R.ok();
    }
}

```

+ AuthRequestFilter.java

  ```java
  public class AuthRequestFilter extends OncePerRequestFilter implements Ordered {
    private RedisTemplate redisTemplate;
  
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
  ```


    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String token = request.getHeaders(AuthConstants.JWT_TOKEN_HEADER);
        if (StrUtil.isBlank(token)) {
             chain.doFilter(request,response);
             return;
        }
        token = token.replace(AuthConstants.JWT_TOKEN_PREFIX, Strings.EMPTY);
        JWSObject jwsObject = JWSObject.parse(token);
        String payload = jwsObject.getPayload().toString();
    
        // 黑名单token(登出、修改密码)校验
        JSONObject jsonObject = JSONUtil.parseObj(payload);
        String jti = jsonObject.getStr("jti"); // JWT唯一标识
    
        Boolean isBlack = redisTemplate.hasKey(AuthConstants.TOKEN_BLACKLIST_PREFIX + jti);
        if (isBlack) {
    
            response.setStatusCode(HttpStatus.OK);
            response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Cache-Control", "no-cache");
            String body = JSONUtil.toJsonStr(R.failed(ResultCode.INVALID_TOKEN_OR_EXPIRED));
            ServletUtils.renderString(response,body);
            return;
        }



        chain.doFilter(request,response);
    
    }
    
    @Override
    public int getOrder() {
        return 0;
    }
}
  ```

### 2.Redis形式

+ **AuthController.java**

​```java
@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor

public class AuthController {

	private final TokenStore tokenStore;

	private final ApplicationEventPublisher publisher;

	/**
	 * 退出token
	 * @param authHeader Authorization
	 */
	@DeleteMapping("/logout")
	public R<Void> logout(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {
		if (StrUtil.isBlank(authHeader)) {
			return R.failed(SysResultCode.FORBIDDEN, "退出失败，token 为空");
		}

		String tokenValue = authHeader.replace(OAuth2AccessToken.BEARER_TYPE, StrUtil.EMPTY).trim();
		OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
		if (accessToken == null || StrUtil.isBlank(accessToken.getValue())) {
			return R.failed(SysResultCode.FORBIDDEN, "退出失败，token 无效");
		}

		OAuth2Authentication auth2Authentication = tokenStore.readAuthentication(accessToken);

		// 清空access token
		tokenStore.removeAccessToken(accessToken);
		// 清空 refresh token
		OAuth2RefreshToken refreshToken = accessToken.getRefreshToken();
		if (refreshToken!=null){
			tokenStore.removeRefreshToken(refreshToken);
		}
		// 发布用户登出事件
		//publisher.publishEvent(new LogoutSuccessEvent(auth2Authentication));

		return R.ok();
	}

  ```



##  四、授权码模式页面定制

#### 登录页面重定制

默认的登录页面，如下图：

![img](https://pics2.baidu.com/feed/314e251f95cad1c8136caadb6d7af703cb3d51c1.png?token=7f9dca6be98bb5c62ea0889115d76e7b)

如何定制?

1.定制页面

![img](https://pics0.baidu.com/feed/9e3df8dcd100baa17da1146b59542918c9fc2e1b.png?token=b5e844a5adf80ada60489b231582ba03)

使用thymeleaf进行渲染。

2.定义跳转接口

```java
@ApiOperation(value = "表单登录跳转页面")@GetMapping("/oauth/login")public String loginPage(Model model){    //返回跳转页面   
    return "oauth-login";}
```

3.Spring Security 中配置 

- loginProcessingUrl：这个是定义的form表单提交的url。
- loginPage：这个是定义跳转登录页面的url。

```java
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().formLogin().loginPage("/oauth/login").loginProcessingUrl("/form/login").and().authorizeRequests().antMatchers(AUTHORIZE_ENDPOINT_PATH)
				.authenticated();
	}
```

4.查看效果

![img](https://pics4.baidu.com/feed/908fa0ec08fa513d42f37fbb2a29c5f1b3fbd9cc.png?token=66021a4d978ae8501c5819eb9ac55431)

#### 授权页面重定制

默认的授权页面什么熊样，如下图:

![img](https://pics7.baidu.com/feed/2934349b033b5bb55573043d2f974533b700bc1f.png?token=1437346ae67ce04fcbf81d1e67d12210)

1.定制页面

![img](https://pics1.baidu.com/feed/eaf81a4c510fd9f96ad388f73c6944202934a433.png?token=c3e1d103b0ed5f0822620aa31370ae2a)

2.定义接口跳转

授权页面的跳转接口url：/oauth/confirm_access，这个接口定义在org.springframework.security.oauth2.provider.endpoint.WhitelabelApprovalEndpoint中，如下：

```java

	@RequestMapping("/oauth/confirm_access")
	public ModelAndView getAccessConfirmation(Map<String, Object> model, HttpServletRequest request) throws Exception {
		final String approvalContent = createTemplate(model, request);
		if (request.getAttribute("_csrf") != null) {
			model.put("_csrf", request.getAttribute("_csrf"));
		}
		View approvalView = new View() {
			@Override
			public String getContentType() {
				return "text/html";
			}

			@Override
			public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
				response.setContentType(getContentType());
				response.getWriter().append(approvalContent);
			}
		};
		return new ModelAndView(approvalView, model);
	}
```

自定义也很简单，只需要模仿这个接口自定义一个将其覆盖即可.

注意：@SessionAttributes("authorizationRequest")这个注解一定要标注，授权请求信息是存储在session中。

3.修改默认映射地址

若不修改映射地址 ，则无需执行此步骤

默认的跳转接口是：/oauth/confirm_access 若想变动则需要进行如下配置

```java
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
		//授权页面url
		endpoints.pathMapping("/oauth/confirm_access","custom/confirm_access");
    }
```

按照上述3个步骤即可轻松的实现授权页面自定义，效果如下

![img](https://pics6.baidu.com/feed/a8ec8a13632762d08f2e9654bda898f0503dc693.png?token=7d816da7d148f406b86c1f4dcb32e4d3)

#### 异常页面重定制

异常页面什么意思呢?授权码的请求url如下

```java
http://localhost:9001/blog-auth-server/oauth/authorize?client_id=mugu&response_type=code&scope=all&redirect_uri=http://www.baidu.com

```

假设我将的租户id(client_id)修改成数据库中不存在的值，那么将会触犯异常页面，页面如下：

![img](https://pics7.baidu.com/feed/c8ea15ce36d3d53905d13f2428c3795a342ab033.png?token=56f14401fd246335ac0529d2e968ea03)

这个异常页面是不是不太符合系统的要求，肯定是要自定义的。

1.定制页面

![img](https://pics4.baidu.com/feed/77c6a7efce1b9d16da381294e99a24858d546412.png?token=09b553af186a75f9d1bbeafe6f00426b)

2.定义跳转地址

这个跳转的接口的逻辑在AuthorizationEndpoint中

```java
	private ModelAndView handleException(Exception e, ServletWebRequest webRequest) throws Exception {

		ResponseEntity<OAuth2Exception> translate = getExceptionTranslator().translate(e);
		webRequest.getResponse().setStatus(translate.getStatusCode().value());

		if (e instanceof ClientAuthenticationException || e instanceof RedirectMismatchException) {
			return new ModelAndView(errorPage, Collections.singletonMap("error", translate.getBody()));
		}

		AuthorizationRequest authorizationRequest = null;
		try {
			authorizationRequest = getAuthorizationRequestForError(webRequest);
			String requestedRedirectParam = authorizationRequest.getRequestParameters().get(OAuth2Utils.REDIRECT_URI);
			String requestedRedirect = redirectResolver.resolveRedirect(requestedRedirectParam,
					getClientDetailsService().loadClientByClientId(authorizationRequest.getClientId()));
			authorizationRequest.setRedirectUri(requestedRedirect);
			String redirect = getUnsuccessfulRedirect(authorizationRequest, translate.getBody(), authorizationRequest
					.getResponseTypes().contains("token"));
			return new ModelAndView(new RedirectView(redirect, false, true, false));
		}
		catch (OAuth2Exception ex) {
			// If an AuthorizationRequest cannot be created from the incoming parameters it must be
			// an error. OAuth2Exception can be handled this way. Other exceptions will generate a standard 500
			// response.
			return new ModelAndView(errorPage, Collections.singletonMap("error", translate.getBody()));
		}

	}
```

因此只需要重新定义一个接口进行跳转即可，如下：

```java
@ApiOperation(value = "处理授权异常的跳转页面")@GetMapping("/oauth/error")public String error(Model model){    return "oauth-error";}

```

3.修改默认的映射地址

若不修改映射地址 ，则无需执行此步骤

默认的映射地址为/oauth/error，修改也很简单，只需要在OAuth2的认证服务的配置类：继承AuthorizationServerConfigurerAdapter的配置中修改一下配置

```java
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
		//授权页面url
		endpoints.pathMapping("/oauth/error","custom/error");
    }
```

按照上述3个步骤即可轻松的实现异常页面自定义，效果如下：

![img](https://pics5.baidu.com/feed/96dda144ad345982e21da35917b0a1a7caef8430.png?token=0764967356509c142d772c03e249c3f8)

## 五、多用户体系刷新模式

如果只是针对一种用户体系以及一种认证方式(用户名/手机号/openid)的话，比如`验证码` 模式的扩展，就不需要对`刷新模式`做调整。

但是如果是多用户体系或者多种认证方式，就必须做些调整来适配。

### 1. 原理

`刷新模式` 时序图如下，相较于密码模式还只是 `Granter` 和 `Provider`的变动。

![](https://cdn.jsdelivr.net/gh/xiaoashuo/pics/oauth2/20220729113135.png)

着重说一下刷新模式的认证提供者 `PreAuthenticatedAuthenticationProvider` ，其 authenticate() 认证方法只做**用户状态校验**，check() 方法调用 AccountStatusUserDetailsChecker#check(UserDetails)。

![](https://cdn.jsdelivr.net/gh/xiaoashuo/pics/oauth2/20220729113159.png)

注意 下`this.preAuthenticatedUserDetailsService.loadUserDetails((PreAuthenticatedAuthenticationToken)authentication);` 的 `preAuthenticatedUserDetailsService` 用户服务。

在没有进行授权模式扩展的时候，是下面这样设置的

![](https://cdn.jsdelivr.net/gh/xiaoashuo/pics/oauth2/20220729113219.png)

然后在 `AuthorizationServerEndpointsConfigurer#addUserDetailsService(DefaultTokenServices,UserDetailsService)` 构造 `PreAuthenticatedAuthenticationProvider` 里设置了 `UserDetailService`用户服务。

![](https://cdn.jsdelivr.net/gh/xiaoashuo/pics/oauth2/20220729113256.png)



这样在多用户体系认证下问题可想而知，用户分别有系统用户和会员用户，这里固定成一个用户服务肯定是行不通的。

### 2.实战

首先我们清楚一个 OAuth2 客户端基本对应的是一个用户体系

| OAuth2 客户端名称 | OAuth2 客户端ID | 用户体系 |
| ----------------- | --------------- | -------- |
| 管理系统          | admin           | 系统用户 |
| 移动端            | client          | 会员用户 |

那就有一个很简单有效的思路，可以在系统内部维护一个如上表的映射关系 Map，然后根据传递的客户端ID去选择用户体系或刷新模式。

#### 思路1： 

#####    1.创建provider指定用户体系

扩展授权模式创建 `Provider` 时可以指定具体的用户服务 `UserDetailService`，就如下面这样：

![](https://cdn.jsdelivr.net/gh/xiaoashuo/pics/oauth2/20220729113315.png)

##### 2.刷新模式指定客户体系调用

你可以为每个授权模式扩展新增对应的刷新模式，但是这样的话比较麻烦，本文的实现方案核心图的是简单有效，所以这里使用的另一种方案，重新设置`PreAuthenticatedAuthenticationProvider` 的 preAuthenticatedUserDetailsService 属性，让其有判断选择用户体系和认证方式的能力。

新增的 PreAuthenticatedUserDetailsService 可根据客户端和认证方式选择UserDetailService 和方法获取用户信息 UserDetail

```java
/**
 * 刷新token再次认证 UserDetailsService
 *
 * @author <a href="mailto:xianrui0365@163.com">xianrui</a>
 * @date 2021/10/2
 */
@NoArgsConstructor
public class PreAuthenticatedUserDetailsService<T extends Authentication> implements AuthenticationUserDetailsService<T>, InitializingBean {

    /**
     * 客户端ID和用户服务 UserDetailService 的映射
     *
     * @see com.youlai.auth.security.config.AuthorizationServerConfig#tokenServices(AuthorizationServerEndpointsConfigurer)
     */
    private Map<String, UserDetailsService> userDetailsServiceMap;

    public PreAuthenticatedUserDetailsService(Map<String, UserDetailsService> userDetailsServiceMap) {
        Assert.notNull(userDetailsServiceMap, "userDetailsService cannot be null.");
        this.userDetailsServiceMap = userDetailsServiceMap;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.userDetailsServiceMap, "UserDetailsService must be set");
    }

    /**
     * 重写PreAuthenticatedAuthenticationProvider 的 preAuthenticatedUserDetailsService 属性，可根据客户端和认证方式选择用户服务 UserDetailService 获取用户信息 UserDetail
     *
     * @param authentication
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserDetails(T authentication) throws UsernameNotFoundException {
        String clientId = RequestUtils.getOAuth2ClientId();
        // 获取认证方式，默认是用户名 username
        AuthenticationMethodEnum authenticationMethodEnum = AuthenticationMethodEnum.getByValue(RequestUtils.getAuthenticationMethod());
        UserDetailsService userDetailsService = userDetailsServiceMap.get(clientId);
        if (clientId.equals(SecurityConstants.APP_CLIENT_ID)) {
            // 移动端的用户体系是会员，认证方式是通过手机号 mobile 认证
            MemberUserDetailsServiceImpl memberUserDetailsService = (MemberUserDetailsServiceImpl) userDetailsService;
            switch (authenticationMethodEnum) {
                case MOBILE:
                    return memberUserDetailsService.loadUserByMobile(authentication.getName());
                default:
                    return memberUserDetailsService.loadUserByUsername(authentication.getName());
            }
        }  else if (clientId.equals(SecurityConstants.ADMIN_CLIENT_ID)) {
            // 管理系统的用户体系是系统用户，认证方式通过用户名 username 认证
            switch (authenticationMethodEnum) {
                default:
                    return userDetailsService.loadUserByUsername(authentication.getName());
            }
        } else {
            return userDetailsService.loadUserByUsername(authentication.getName());
        }
    }
}
```

AuthorizationServerConfig 配置重新设置 PreAuthenticatedAuthenticationProvider 的 preAuthenticatedUserDetailsService 属性值

```java
  /**
     * 配置授权（authorization）以及令牌（token）的访问端点和令牌服务(token services)
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        // Token增强
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> tokenEnhancers = new ArrayList<>();
        tokenEnhancers.add(tokenEnhancer());
        tokenEnhancers.add(jwtAccessTokenConverter());
        tokenEnhancerChain.setTokenEnhancers(tokenEnhancers);

        // 获取原有默认授权模式(授权码模式、密码模式、客户端模式、简化模式)的授权者
        List<TokenGranter> granterList = new ArrayList<>(Arrays.asList(endpoints.getTokenGranter()));

        // 添加验证码授权模式授权者
        granterList.add(new CaptchaTokenGranter(endpoints.getTokenServices(), endpoints.getClientDetailsService(),
                endpoints.getOAuth2RequestFactory(), authenticationManager, stringRedisTemplate
        ));

        // 添加手机短信验证码授权模式的授权者
        granterList.add(new SmsCodeTokenGranter(endpoints.getTokenServices(), endpoints.getClientDetailsService(),
                endpoints.getOAuth2RequestFactory(), authenticationManager
        ));



        CompositeTokenGranter compositeTokenGranter = new CompositeTokenGranter(granterList);
        endpoints
                .authenticationManager(authenticationManager)
                .accessTokenConverter(jwtAccessTokenConverter())
                .tokenEnhancer(tokenEnhancerChain)
                .tokenGranter(compositeTokenGranter)
                /** refresh token有两种使用方式：重复使用(true)、非重复使用(false)，默认为true
                 *  1 重复使用：access token过期刷新时， refresh token过期时间未改变，仍以初次生成的时间为准
                 *  2 非重复使用：access token过期刷新时， refresh token过期时间延续，在refresh token有效期内刷新便永不失效达到无需再次登录的目的
                 */
                .reuseRefreshTokens(true)
                .tokenServices(tokenServices(endpoints))
        ;
    }


    public DefaultTokenServices tokenServices(AuthorizationServerEndpointsConfigurer endpoints) {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> tokenEnhancers = new ArrayList<>();
        tokenEnhancers.add(tokenEnhancer());
        tokenEnhancers.add(jwtAccessTokenConverter());
        tokenEnhancerChain.setTokenEnhancers(tokenEnhancers);

        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(endpoints.getTokenStore());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setClientDetailsService(clientDetailsService);
        tokenServices.setTokenEnhancer(tokenEnhancerChain);

        // 多用户体系下，刷新token再次认证客户端ID和 UserDetailService 的映射Map
        Map<String, UserDetailsService> clientUserDetailsServiceMap = new HashMap<>();
        clientUserDetailsServiceMap.put(SecurityConstants.ADMIN_CLIENT_ID, sysUserDetailsService); // 管理系统客户端
        clientUserDetailsServiceMap.put(SecurityConstants.APP_CLIENT_ID, memberUserDetailsService); // Android/IOS/H5 移动客户端

        // 重新设置PreAuthenticatedAuthenticationProvider#preAuthenticatedUserDetailsService 能够根据客户端ID和认证方式区分用户体系获取认证用户信息
        PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
        provider.setPreAuthenticatedUserDetailsService(new PreAuthenticatedUserDetailsService<>(clientUserDetailsServiceMap));
        tokenServices.setAuthenticationManager(new ProviderManager(Arrays.asList(provider)));
        return tokenServices;
    }
```

#### 思路2：

1.注册`ClientHandlerConfigurer`的Bean

```java
@Component
public class ClientInfoService implements ClientHandlerConfigurer {
    @Override
    public void Client(Map<String, UserDetailsService> clientMap) {
        //客户端与用户体系的
           clientMap.put("admin",new AdminUserService());
        clientMap.put("client",new ClientUserService());
    }

    @Override
    public void grantTyp(Map<String, RetriveUserFunction> grantTypeMap) {
       //配置 授权方式 与对应的处理逻辑
grantTypeMap.put("password", new RetriveUserFunction() {
			@Override
			public <T extends Authentication> UserDetails retrive(T authentication,
					UserDetailsService userDetailsService) {
				String name = authentication.getName();
				return userDetailsService.loadUserByUsername(name);
			}
		});
		grantTypeMap.put("sms_code", new RetriveUserFunction() {
			@Override
			public <T extends Authentication> UserDetails retrive(T authentication,
					UserDetailsService userDetailsService) {
				String name = authentication.getName();
				ExtendUserDetailsService extendUserDetailsService = (ExtendUserDetailsService) userDetailsService;
				return extendUserDetailsService.loginByMobile(name);
			}
		});
    }
}

```

2.注册 `UserDetailService`代理

> 注:若不注册此代理 则多用户体系不生效

```java
	@Bean
	public UserService userService(AuthorizationInfoHandle authorizationInfoHandle){
		return ProxyFactory.create(UserService.class, new PreMethodInterceptor(authorizationInfoHandle));
	}
```

