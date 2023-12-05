package com.accounting.accountingservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.accounting.accountingservice.models.Graph;
import com.accounting.accountingservice.GraphService;

@Controller
@RequestMapping("/graphs")

public class GraphController {

    private final GraphService graphService;


    @Autowired
    public GraphController(GraphService graphService) {
        this.graphService = graphService;

    }

    @GetMapping()
    public String index(Model model, @RequestParam(value = "graph", required = false) Integer graph,
                        @RequestParam(value = "hours", required = false) Integer hours,
                        @RequestParam(value = "startTime", required = false) boolean startTime) {

        if (graph == null || hours == null)
            model.addAttribute("graph", graphService.findAll(startTime)); // выдача всех книг
        else
            model.addAttribute("graph", graphService.findWithPagination(graph, hours, startTime));

        return "graph/graphs";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, User user, @ModelAttribute("user") User user) {
        user.addAttribute("graph", graphService.findOne(id));

        User graphForUser = graphService.getGraphForUser(id);

        if (graphForUser != null)
            user.addAttribute("owner", graphForUser);


        return "graph/show";
    }

    @GetMapping("/new")
    public String newGraph(@ModelAttribute("graph") Graph Graph) {
        return "graph/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("graph") @Valid Graph Graph,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "graph/new";

        graphService.save(Graph);
        return "redirect:/graph";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("graph", graphService.findOne(id));
        return "graph/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("graph") @Valid Graph graph, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "graph/edit";

        graphService.update(id, graph);
        return "redirect:/graph";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        graphService.delete(id);
        return "redirect:/graph";
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id) {
        graphService.release(id);
        return "redirect:/graph/" + id;
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id, @ModelAttribute("user") User selectedUser) {

        graphService.assign(id, selectedUser);
        return "redirect:/graph/" + id;
    }

    @GetMapping("/search")
    public String searchPage() {
        return "graph/search";
    }

    @PostMapping("/search")
    public String makeSearch(Model model, @RequestParam("query") String query) {
        model.addAttribute("graph", graphService.searchByTitle(query));
        return "graph/search";
    }
}

