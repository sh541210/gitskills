package com.iyin.sign.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iyin.sign.system.entity.FileResource;
import com.iyin.sign.system.mapper.FileResourceMapper;
import com.iyin.sign.system.model.IyinPage;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.service.FileResourceService;
import com.iyin.sign.system.vo.file.FileResourceDto;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 文件微服务中文件资源表 服务实现类
 * </p>
 *
 * @author xiaofanzhou
 * @since 2018-09-14
 */
@Service
public class FileResourceServiceImpl extends ServiceImpl<FileResourceMapper, FileResource> implements FileResourceService {

	@Resource
	private FileResourceMapper fileResourceMapper;

	@Override
	public FileResource findByFileCode(String fileCode) {
		QueryWrapper<FileResource> queryWrapper = new QueryWrapper<FileResource>().eq("file_code", fileCode);
		return getOne(queryWrapper);
	}

	@Override
	public IyinResult<IyinPage<FileResourceDto>> getDocList(Integer currPage, Integer pageSize, String fileName) {
		IyinPage<FileResourceDto> page = new IyinPage<>(currPage, pageSize);
		FileResourceDto fileResourceDto=new FileResourceDto();
		if(StringUtils.isNotBlank(fileName)){
			fileResourceDto.setFileName(fileName);
		}
		List<FileResourceDto> fileResourceDtos=fileResourceMapper.getPageList(page,fileResourceDto);
		page.setRecords(fileResourceDtos);
		//返回请求结果
		IyinResult<IyinPage<FileResourceDto>> result = IyinResult.success();
		result.setData(page);
		return result;
	}


}
