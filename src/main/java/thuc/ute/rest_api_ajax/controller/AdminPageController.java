package thuc.ute.rest_api_ajax.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminPageController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping({"/admin/category", "/admin/categories"})
    public String categoryPage() {
        return "admin/category";
    }

    @GetMapping({"/admin/product", "/admin/products"})
    public String productPage() {
        return "admin/product";
    }
}
