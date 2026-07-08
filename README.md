# swing-demo-app — Java Swing 演示客户端（用于 ai-test-platform 测试）

本项目是 AI Test Design Platform 的演示客户端：一个简单的 Java Swing 登录界面，供人工演示与自动化测试（通过 AssertJ Swing 驱动）使用。

要点摘要：
- 所有可交互组件均调用 `setName()`（例如 `username`、`password`、`loginButton`），便于断言与 Screen Metadata 导出。
- 默认后端地址为 `http://localhost:8000`（由 `LoginApp` 中的 `BackendApiClient` 构造时传入），通常配合 `fastapi-mock` 使用。
- 支持 JSON 驱动的测试（样例见 `test-runner/src/main/resources/login-test.json`）。

**兼容与依赖**
- JDK: Java 11 或更高（项目 pom.xml 使用 `maven.compiler.source` = 11）。
- 构建工具: Maven

**主要源码**
- `src/main/java/com/example/LoginApp.java`：登录窗口与事件逻辑（组件名称、UI 布局、调用后端）。
- `src/main/java/com/example/HomePageApp.java`：登录成功后的主页示例。
- `src/main/java/com/example/GlobalContext.java`：全局上下文（当前登录用户名）。
- `src/main/java/com/example/BackendApiClient.java`：HTTP 客户端，负责调用 `/login` 接口并解析返回值。

## 快速开始（开发/运行）

1. 使用 IDE（推荐）
	- 在 IDE 中导入 Maven 项目，运行 `com.example.LoginApp` 的 `main` 方法。

2. 使用命令行（Maven Exec）
```bash
mvn compile
mvn exec:java -Dexec.mainClass=com.example.LoginApp
```
如果环境中需要指定插件坐标：
```bash
mvn org.codehaus.mojo:exec-maven-plugin:3.1.0:java -Dexec.mainClass=com.example.LoginApp
```

3. 打包并以 classpath 运行（可在 CI 或演示环境使用）
```bash
mvn -DskipTests package
java -cp target/classes com.example.LoginApp
```

## 与后端（fastapi-mock）集成
- 默认 `LoginApp` 中使用 `new BackendApiClient("http://localhost:8000")` 发起请求，建议先启动 `fastapi-mock`：

```bash
cd fastapi-mock
# (可选) 创建并激活虚拟环境，然后安装依赖
pip install -r requirements.txt
uvicorn app.main:app --reload --host 0.0.0.0 --port 8000
```

后端启动后，`LoginApp` 的登录操作会调用 `POST /login` 并根据返回的 JSON 决定成功或失败。

如果需要更改后端地址，请编辑 `src/main/java/com/example/LoginApp.java` 中构造 `BackendApiClient` 的参数。

## 自动化测试（AssertJ Swing / JSON 驱动）

项目已配合仓库中的 `test-runner` 模块实现自动化测试。常见流程：

1. 启动 `fastapi-mock`（见上文）。
2. 在另一个终端运行被测应用（或让测试框架以 `appClass` 启动应用）。
3. 进入 `test-runner` 目录并执行：

```bash
cd test-runner
mvn test
```

测试使用的 JSON DSL 示例：`test-runner/src/main/resources/login-test.json`，包含 `input`、`action`、`expectations` 等字段，示例包括正常登录、错误密码和初始显示断言。

## Screen Metadata 与组件命名约定

为了让 AI/工具自动识别界面，窗口中的交互组件应当调用 `setName()` 并使用稳定的标识：

- 文本框：`username`
- 密码框：`password`
- 登录按钮：`loginButton`

这些名称可由 Metadata Exporter 扫描并导出为 `screen.json`，供 AI 生成测试用例时使用。

## 调试与常见问题
- 如果出现 `网络错误` 弹窗，检查 `fastapi-mock` 是否已启动并监听 `http://localhost:8000`。 
- 若测试执行找不到组件，请确认组件已正确调用 `setName()` 且名称与测试中使用的 `target` 匹配。

## 贡献与扩展建议
- 可将后端地址做成配置项（系统属性 / 环境变量），以便在 CI 中无须修改源代码。
- 可为打包发布添加 Maven Shade 插件以生成可执行 fat JAR，简化演示运行命令。

---


