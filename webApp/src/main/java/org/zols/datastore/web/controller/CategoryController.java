package org.zols.datastore.web.controller;

import com.zols.linkmanager.LinkManager;
import com.zols.linkmanager.domain.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Controller for Category
 *
 * @author poomalai
 */
@Controller
public class CategoryController {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(CategoryController.class);

    @Autowired
    private LinkManager linkManager;

    @RequestMapping(value = "/api/links/categories", method = POST)
    @ResponseBody
    public Category create(@RequestBody Category category) {
        LOGGER.info("Creating new categories {}", category);
        return linkManager.add(category);
    }

    @RequestMapping(value = "/api/links/categories/{name}", method = PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@PathVariable(value = "name") String name,
            @RequestBody Category category) {
        LOGGER.info("Updating categories with id {} with {}", name, category);
        if (name.equals(category.getName())) {
            linkManager.update(category);
        }
    }

    @RequestMapping(value = "/api/links/categories/{name}", method = DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(value = "name") String name) {
        LOGGER.info("Deleting categories with id {}", name);
        linkManager.deleteCategory(name);
    }

    @RequestMapping(value = "/links/categories/{name}", method = GET)
    public String edit(@PathVariable(value = "name") String name, Model model) {
        model.addAttribute("category", linkManager.getCategory(name));
        return "datastore/category";
    }

    @RequestMapping(value = "/links/categories/add", method = GET)
    public String add(Model model) {
        model.addAttribute("category", new Category());
        return "datastore/category";
    }

    @RequestMapping(value = "/api/links/categories", method = GET)
    @ResponseBody
    public Page<Category> list(
            Pageable page) {
        LOGGER.info("Listing categories");
        return linkManager.categoriesByPageable(page);
    }

    @RequestMapping(value = "/links/categories", method = GET)
    public String listing() {
        return "datastore/listcategories";
    }

}
