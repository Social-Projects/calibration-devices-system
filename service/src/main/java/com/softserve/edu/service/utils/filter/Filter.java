package com.softserve.edu.service.utils.filter;

import com.softserve.edu.service.utils.filter.internal.Comparison;
import com.softserve.edu.service.utils.filter.internal.Condition;
import com.softserve.edu.service.utils.filter.internal.Type;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * [{
 * "type": "string",
 * "value": "***",
 * "field": "model"
 * },{
 * "type": "numeric",
 * "value": "***",
 * "field": "year",
 * "comparison": "gt"
 * }]
 */
public class Filter implements Specification {

    private List<Condition> conditions;

    public Filter(String json) {
//        ObjectMapper mapper = new ObjectMapper();
//        this.conditions = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, Condition.class));
    }

    public Filter() {
        conditions = new ArrayList<>();
    }

    public Filter(Map<String, Object> searchKeys) {
        conditions = new ArrayList<>();
        for (Map.Entry<String, Object> entry : searchKeys.entrySet()) {
            if (entry.getValue() instanceof String) {
                this.addCondition(new Condition.Builder()
                        .setComparison(Comparison.like)
                        .setField(entry.getKey())
                        .setValue(entry.getValue())
                        .build());
            } else if (entry.getValue() instanceof List) {
//                this.addCondition(new Condition.Builder()
//                        .setComparison(Comparison.ge)
//                        .setType(Type.date)
//                        .setField(entry.getKey())
//                        .setValue(((Map) entry.getValue()).get("startDate"))
//                        .build());
//                this.addCondition(new Condition.Builder()
//                        .setComparison(Comparison.le)
//                        .setType(Type.date)
//                        .setField(entry.getKey())
//                        .setValue(((Map) entry.getValue()).get("endDay"))
//                        .build());
                this.addConditionList(buildBetweenDatesPredicate(entry.getKey(), (List) entry.getValue()));
            } else {
                this.addCondition(new Condition.Builder()
                        .setComparison(Comparison.eq)
                        .setField(entry.getKey())
                        .setValue(entry.getValue())
                        .build());
            }
        }
    }
    public void addCondition(Condition condition) {
        this.conditions.add(condition);
    }

    public void addConditionList(List<Condition> conditions) {
        this.conditions.addAll(conditions);
    }

    public Filter createFilterFromSearchKeys(Map<String, Object> searchKeys) {
        Filter filter = new Filter();
        for (Map.Entry<String, Object> entry : searchKeys.entrySet()) {
            if (entry.getValue() instanceof String) {
                filter.addCondition(new Condition.Builder()
                        .setComparison(Comparison.like)
                        .setField(entry.getKey())
                        .setValue(entry.getValue())
                        .build());
            } else if (entry.getValue() instanceof Map) {
                filter.addCondition(new Condition.Builder()
                        .setComparison(Comparison.ge)
                        .setField(entry.getKey())
                        .setValue(((Map) entry.getValue()).get("startDate"))
                        .build());
                filter.addCondition(new Condition.Builder()
                        .setComparison(Comparison.le)
                        .setField(entry.getKey())
                        .setValue(((Map) entry.getValue()).get("endDay"))
                        .build());
            } else {
                filter.addCondition(new Condition.Builder()
                        .setComparison(Comparison.eq)
                        .setField(entry.getKey())
                        .setValue(entry.getValue())
                        .build());
            }
        }
        return filter;
    }

    @Override
    public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = buildPredicates(root, criteriaQuery, criteriaBuilder);
        return predicates.size() > 1
                ? criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]))
                : predicates.get(0);
    }

    private List<Predicate> buildPredicates(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
//       return conditions.stream().map(this::buildPredicate).collect(toList());
        List<Predicate> predicates = new ArrayList<>();
        conditions.forEach(condition -> predicates.add(buildPredicate(condition, root, criteriaQuery, criteriaBuilder)));
        return predicates;
    }

    private static List<Condition> buildBetweenDatesPredicate(String name, List<Date> dateList) {
        List<Condition> conditions = new ArrayList<>();
        conditions.add(new Condition.Builder()
                .setType(Type.date)
                .setComparison(Comparison.ge)
                .setField(name)
                .setValue(dateList.stream().min(Date::compareTo).get())
                .build());
        conditions.add(new Condition.Builder()
                .setComparison(Comparison.le)
                .setType(Type.date)
                .setField(name)
                .setValue(dateList.stream().max(Date::compareTo).get())
                .build());
        return conditions;
    }
    public Predicate buildPredicate(Condition condition, Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
        switch (condition.comparison) {
            case eq:
                return buildEqualsPredicateToCriteria(condition, root, criteriaQuery, criteriaBuilder);
            case gt:
                return buildGreaterThanPredicateToCriteria(condition, root, criteriaQuery, criteriaBuilder);
            case ge:
                return buildGreaterEqualPredicateToCriteria(condition, root, criteriaQuery, criteriaBuilder);
            case lt:
                return buildLessThanPredicateToCriteria(condition, root, criteriaQuery, criteriaBuilder);
            case le:
                return buildLessEqualPredicateToCriteria(condition, root, criteriaQuery, criteriaBuilder);
            case ne:
                return buildNotEqualPredicateToCriteria(condition, root, criteriaQuery, criteriaBuilder);
            case isnull:
                return buildIsNullPredicateToCriteria(condition, root, criteriaQuery, criteriaBuilder);
            case like:
                return buildLikePredicateToCriteria(condition, root, criteriaQuery, criteriaBuilder);
            case in:
                break;
            default:
                return buildEqualsPredicateToCriteria(condition, root, criteriaQuery, criteriaBuilder);
        }
        throw new RuntimeException();
    }

    private Predicate buildEqualsPredicateToCriteria(Condition condition, Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.equal(root.get(condition.field), condition.value);
    }

    private Predicate buildLikePredicateToCriteria(Condition condition, Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.like(root.get(condition.field), "%" + condition.value + "%");
    }

    private Predicate buildGreaterThanPredicateToCriteria(Condition condition, Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
        if (condition.type.equals(Type.date)) {
            return criteriaBuilder.greaterThan(root.<Date>get(condition.field), (Date) condition.value);
        } else throw new RuntimeException();
    }

    private Predicate buildLessThanPredicateToCriteria(Condition condition, Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
        if (condition.type.equals(Type.date)) {
            return criteriaBuilder.lessThan(root.<Date>get(condition.field), (Date) condition.value);
        } else throw new RuntimeException();
    }

    private Predicate buildGreaterEqualPredicateToCriteria(Condition condition, Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
        if (condition.type.equals(Type.date)) {
            return criteriaBuilder.greaterThanOrEqualTo(root.<Date>get(condition.field), (Date) condition.value);
        } else throw new RuntimeException();
    }

    private Predicate buildLessEqualPredicateToCriteria(Condition condition, Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
        if (condition.type.equals(Type.date)) {
            return criteriaBuilder.lessThanOrEqualTo(root.<Date>get(condition.field), (Date) condition.value);
        } else throw new RuntimeException();
    }

    private Predicate buildNotEqualPredicateToCriteria(Condition condition, Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.notEqual(root.get(condition.field), condition.value);
    }

    private Predicate buildIsNullPredicateToCriteria(Condition condition, Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.isNull(root.get(condition.field));
    }
}
