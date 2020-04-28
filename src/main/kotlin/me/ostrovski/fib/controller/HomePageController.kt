package me.ostrovski.fib.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

/**
 * Redirects from the root to the Swagger spec.
 */
@Controller
class HomePageController {
    @GetMapping("/")
    fun home(): String = "redirect:swagger-ui.html"
}
