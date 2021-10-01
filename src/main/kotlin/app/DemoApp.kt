package app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping

@SpringBootApplication
class DemoApp

fun main(args: Array<String>) {
    runApplication<DemoApp>(*args)
}

@Controller
class HelloController {

    @GetMapping("/hello")
    fun index(model: Model): String {
        // https://docs.spring.io/spring-framework/docs/current/kdoc-api/spring-framework/org.springframework.ui/index.html
        model["message"] = "Hello"
        return "greeting"
    }
}
