package com.springdev.mepdev.JModels;

import com.springdev.mepdev.models.Cours;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Data
public class CoursSpecification  implements Specification<Cours> {

    private SearchCriteria criteria;

    public CoursSpecification(SearchCriteria searchCriteria){
        this.criteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<Cours> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

        if(criteria.getOperation().equalsIgnoreCase(":")){
            return criteriaBuilder.like(
                    root.<String>get(criteria.getKey()),"%"+ criteria.getValue()+"%");


        }
        else {
            return criteriaBuilder.equal(root.<String>get(criteria.getKey()),
                    criteria.getValue());
        }

    }
}
