package #(backendPackage).service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import #(backendPackage).entity.#(entityName);
import #(backendPackage).mapper.#(entityName)Mapper;
import #(backendPackage).service.#(entityName)Service;

@Service
public class #(entityName)ServiceImpl extends ServiceImpl<#(entityName)Mapper, #(entityName)> implements #(entityName)Service {
}
