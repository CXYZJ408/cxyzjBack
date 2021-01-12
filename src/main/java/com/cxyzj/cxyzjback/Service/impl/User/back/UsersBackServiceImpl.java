package com.cxyzj.cxyzjback.Service.impl.User.back;

import com.cxyzj.cxyzjback.Bean.User.User;
import com.cxyzj.cxyzjback.Repository.User.UserJpaRepository;
import com.cxyzj.cxyzjback.Service.Interface.User.back.UsersBackService;
import com.cxyzj.cxyzjback.Utils.Constant;
import com.cxyzj.cxyzjback.Utils.Response;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Log
@Service
public class UsersBackServiceImpl implements UsersBackService {
    private final UserJpaRepository userJpaRepository;

    @Autowired
    public UsersBackServiceImpl(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public String test(String userId, String nickname, String gender) {
        log.info("userId:" + userId);
        log.info("nickname:" + nickname);
        //动态查询
        Specification<User> specification = (Specification<User>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicatesList = new ArrayList<>();
            if (!userId.equals(Constant.NONE)) {
                Predicate userIdPredicate = criteriaBuilder.equal(root.get("userId"), userId);
                predicatesList.add(userIdPredicate);
            }
            if (!nickname.equals(Constant.NONE)) {
                Predicate nicknamePredicate = criteriaBuilder.equal(root.get("nickname"), nickname);
                predicatesList.add(nicknamePredicate);
            }
            if (!gender.equals(Constant.NONE)) {//此处可以抽象出一个工具类，以便处理其他有多个参数的字段
                String genders[] = gender.split(",");
                List<Predicate> genderPredicateList = new ArrayList<>();
                for (String sGender : genders) {
                    Predicate genderPredicate = criteriaBuilder.equal(root.get("gender"), sGender);
                    genderPredicateList.add(genderPredicate);
                }
                Predicate[] tmp = new Predicate[predicatesList.size()];
                predicatesList.add(criteriaBuilder.or(genderPredicateList.toArray(tmp)));
            }
            query.orderBy(criteriaBuilder.asc(root.get("registDate")));
            Predicate[] predicates = new Predicate[predicatesList.size()];
            return criteriaBuilder.and(predicatesList.toArray(predicates));
        };
        List<User> list = userJpaRepository.findAll(specification);
        Response response = new Response();
        response.insert("list", list);
        return response.sendSuccess();
    }
}
