package com.accounting.accountingservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.accounting.accountingservice.models.Graph;
import com.accounting.accountingservice.User;
import com.accounting.accountingservice.GraphRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class GraphService {

    private final GraphRepository graphRepository;

    @Autowired
    public GraphService(GraphRepository graphRepository) {
        this.graphRepository = graphRepository;
    }

    public List<Graph> findAll(boolean sortByType) {
        if (sortByType)
            return graphRepository.findAll(Sort.by("type"));
        else
            return graphRepository.findAll();
    }

    public List<Graph> findWithPagination(Integer type, Integer graphForType, boolean sortByType) {
        if (sortByType)
            return graphRepository.findAll(PageRequest.of(type, graphForType, Sort.by("type"))).getContent();
        else
            return graphRepository.findAll(PageRequest.of(type, graphForType)).getContent();
    }

    public Graph findOne(int id) {
        Optional<Graph> foundBook = graphRepository.findById(id);
        return foundGraph.orElse(null);
    }

    public List<Graph> searchByUser(String query) {
        return graphRepository.findByUserStartingWith(query);
    }

    @Transactional
    public void save(Graph graph) {
        graphRepository.save(graph);
    }

    @Transactional
    public void update(int id, Graph updatedBook) {
        Graph graphToBeUpdated = graphRepository.findById(id).get();


        updatedGraph.setId(id);
        updatedGraph.setOwner(graphToBeUpdated.getUser());

        graphRepository.save(updatedGraph);
    }

    @Transactional
    public void delete(int id) {
        graphRepository.deleteById(id);
    }


    public User getGraphOwner(int id) {

        return graphRepository.findById(id).map(Graph::getUser).orElse(null);
    }


    @Transactional
    public void release(int id) {
        graphRepository.findById(id).ifPresent(
                graph -> {
                    graph.setUser(null);
                    graph.setTakenAt(null);
                });
    }


    @Transactional
    public void assign(int id, User selectedUser) {
        graphRepository.findById(id).ifPresent(
                graph -> {
                    graph.setUser(selectedUser);
                    graph.setTakenAt(new Date());
                }
        );
    }
}