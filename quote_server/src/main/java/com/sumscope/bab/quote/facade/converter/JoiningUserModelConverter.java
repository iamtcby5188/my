package com.sumscope.bab.quote.facade.converter;

import com.sumscope.bab.quote.commons.enums.BABJoiningTarget;
import com.sumscope.bab.quote.commons.util.ValidationUtil;
import com.sumscope.bab.quote.externalinvoke.EdmHttpClientHelperWithCache;
import com.sumscope.bab.quote.externalinvoke.IAMEntitlementCheck;
import com.sumscope.bab.quote.facade.ApplicationFacadeService;
import com.sumscope.bab.quote.model.dto.*;
import com.sumscope.bab.quote.model.model.JoiningUserModel;
import com.sumscope.iam.edmclient.edmsource.dto.IdbFinancialCompanyAndIdDTO;
import com.sumscope.iam.edmclient.edmsource.dto.UserContactInfoRetDTO;
import com.sumscope.iam.edmclient.edmsource.dto.UserRetDTO;
import com.sumscope.iam.iamclient.model.AccessTokenResultDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JoiningUserModelConverter {
	@Autowired
	private EdmHttpClientHelperWithCache edmHttpClientHelperWithCache;
	@Autowired
	private IAMEntitlementCheck iamEntitlementCheck;

	@Autowired
	private ApplicationFacadeService applicationFacadeService;

	public JoiningUserModel convertToModel(UserJoiningRelationDto dto) {
		if(!dto.isDelJoinUser()){
			AccessTokenResultDto accessTokenResultDto = iamEntitlementCheck.getAccessTokenResultDto(null, dto.getJoiningUserDto());
			dto.getJoiningUserDto().setID(accessTokenResultDto.getUserId());
		}
	   //逻辑为： CHILD模式，当前用户是model.userId, dto中的关联用户是model.joinUserId
	   //PARENT模式：当前用户是model.joinUserId, dto中的关联用户是model.userId
		ValidationUtil.validateModel(dto);
		JoiningUserModel joiningUserModel=new JoiningUserModel();
		joiningUserModel.setJoinMode(dto.getJoiningMode());
		joiningUserModel.setJoinUserId(dto.getJoiningUserDto().getID());
		if(BABJoiningTarget.CHILD==dto.getJoingTarget()){
			joiningUserModel.setUserId(dto.getCurrentUserId());
		}else if(BABJoiningTarget.PARENT==dto.getJoingTarget()){
			joiningUserModel.setUserId(dto.getJoiningUserDto().getID());
			joiningUserModel.setJoinUserId(dto.getCurrentUserId());
		}else{
			joiningUserModel.setUserId(dto.getCurrentUserId());
		}
		return joiningUserModel;
	}

	/**
	 * JoiningUserModel 转 AvailableContactDto
	 * @param model
	 * @return
     */
	public AvailableContactDto convertJoiningUserModelToDto(JoiningUserModel model) {
		AvailableContactDto dto=new AvailableContactDto();
		dto.setJoiningDisplayMode(model.getJoinMode());

		IAMCompanyReferenceDto companyDto=new IAMCompanyReferenceDto();
		IdbFinancialCompanyAndIdDTO companyInfoByID = edmHttpClientHelperWithCache.getIdbFinancialCompanyAndIdDTO(edmHttpClientHelperWithCache.getUserRetDTO(model.getJoinUserId()).getCompanyId());
		companyDto.setID(companyInfoByID.getId());
		companyDto.setProvince(companyInfoByID.getCityName());
		companyDto.setName(companyInfoByID.getName());
		companyDto.setBankNature(companyInfoByID.getBankNature());
		companyDto.setFullName(companyInfoByID.getFullName());
		dto.setJoiningCompanyDto(companyDto);

		IAMUserReferenceDto iamUserReferenceDto=new IAMUserReferenceDto();
		UserContactInfoRetDTO userContactByID = edmHttpClientHelperWithCache.getUserContactInfoRetDTO(model.getJoinUserId());
		UserRetDTO userByID = edmHttpClientHelperWithCache.getUserRetDTO(model.getJoinUserId());
		iamUserReferenceDto.setMobileTel(UserMobileTelConverter.getUserMobileTel(userContactByID));
		iamUserReferenceDto.setQq(userContactByID.getQq().contains(";") ? userContactByID.getQq().replace(";","") : userContactByID.getQq());
		iamUserReferenceDto.setName(userByID.getName());
		iamUserReferenceDto.setID(userByID.getId());
		iamUserReferenceDto.setUserName(userByID.getUsername());
		dto.setJoiningUserDto(iamUserReferenceDto);
		return dto;
	}

	public List<AvailableContactDto> convertJoiningUserModelToDto(List<JoiningUserModel> joiningUserModelList) {
		List<AvailableContactDto> list = new ArrayList<>();
		if (joiningUserModelList != null && joiningUserModelList.size() > 0) {
			for (JoiningUserModel joiningUserModel : joiningUserModelList) {
				AvailableContactDto availableContactDto = convertJoiningUserModelToDto(joiningUserModel);
				list.add(availableContactDto);
			}
		}
		return list;
	}

	public JoinUserResponseDto convertJoiningUserModelToDtos(List<JoiningUserModel> joiningUserModelList,
															 List<JoiningUserModel> joiningModel,AccessTokenResultDto accessTokenResult ) {
		List<AvailableContactDto> list = converterJoinUserToAvailableDtoList(joiningUserModelList, BABJoiningTarget.CHILD);
		List<AvailableContactDto> availableContactDto = converterJoinUserToAvailableDtoList(joiningModel, BABJoiningTarget.PARENT);
		list.addAll(availableContactDto);
		JoinUserResponseDto dto = new JoinUserResponseDto();
		dto.setAvailableContactDto(list);
		AppInitialDataDto appInitialDataDto = applicationFacadeService.loginUser(accessTokenResult);
		appInitialDataDto.setInfo(null);
		dto.setUserInfo(appInitialDataDto);
		return dto;
	}

	private List<AvailableContactDto> converterJoinUserToAvailableDtoList(List<JoiningUserModel> joiningModel,BABJoiningTarget babJoiningTarget) {
		List<AvailableContactDto> list = new ArrayList<>();
		if (joiningModel != null && joiningModel.size() > 0) {
			for (JoiningUserModel joiningUserModel : joiningModel) {
				if (babJoiningTarget == BABJoiningTarget.PARENT) {
					joiningUserModel.setJoinUserId(joiningUserModel.getUserId());
					joiningUserModel.setUserId(joiningUserModel.getJoinUserId());
				}
				AvailableContactDto availableContactDto = convertJoiningUserModelToDto(joiningUserModel);
				availableContactDto.setBabJoiningTarget(babJoiningTarget == BABJoiningTarget.PARENT ? BABJoiningTarget.PARENT : BABJoiningTarget.CHILD);
				list.add(availableContactDto);
			}
		}
		return list;
	}

}
