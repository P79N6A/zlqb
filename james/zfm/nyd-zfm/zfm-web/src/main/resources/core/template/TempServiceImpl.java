package ${modulePackage}.service.impl${moduleName};

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ${modulePackage}.dao${moduleName}.${className}Mapper;
import ${modulePackage}.service.api${moduleName}.${className}Service;

@Service
@Transactional
public class ${className}ServiceImpl<T> implements ${className}Service<T> {

	@Autowired
    private ${className}Mapper<T> mapper;

	public ${className}Mapper<T> getMapper() {
		return mapper;
	}
}
