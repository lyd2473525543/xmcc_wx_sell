package com.xmcc.repository;


import com.xmcc.entity.OrderMaster;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface OrderMasterRepository extends JpaRepository<OrderMaster,String>,JpaSpecificationExecutor<OrderMaster> {
    List<OrderMaster> findAllByBuyerOpenid(String openid, Pageable pageable);
}
