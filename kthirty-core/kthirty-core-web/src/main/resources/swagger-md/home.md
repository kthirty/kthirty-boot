## Kthirty 接口文档系统

> **_此文档必看！！！！_**

------

- [接口认证说明](#接口认证)
- [接口响应说明](#接口响应)

### 接口认证

输入正确的`username`和`password` 后点击登陆即可自动获取token并填入请求头中，其中`clientId`与`clientSecret`暂时未集成，随意填写即可

------

### 接口响应

本系统统一响应格式为

| 字段    | 描述     | 备注                   |
| ------- | -------- | ---------------------- |
| code    | 响应码   | `AA0000`为成功         |
| data    | 承载数据 | 即接口文档中的响应示例 |
| message | 响应消息 |                        |
| success | 是否成功 | true/false             |


```json
{
  "code": "AA0000", 
  "data": null,
  "message": "操作成功",
  "success": true
}
```

所有文档中的 **响应示例** 与 **响应参数**均为`data`中的值，示例如下

若接口文档中 **响应示例** 为

```json
{
  "orgId": 0,
  "orgName": "",
  "permissionsIds": "",
  "permissionsNames": "",
  "realName": "",
  "roleIds": "",
  "roleNames": "",
  "userId": 0,
  "userName": ""
}
```

则接口实际响应为

```json
{
  "code": "AA0000",
  "data": {
    "orgId": 0,
    "orgName": "",
    "permissionsIds": "",
    "permissionsNames": "",
    "realName": "",
    "roleIds": "",
    "roleNames": "",
    "userId": 0,
    "userName": ""
  },
  "message": "",
  "success": true
}
```