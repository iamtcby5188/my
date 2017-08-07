package com.sumscope.bab.quote.model.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 公开市场净投放图表显示dto
 */
public class NetVolumeOpenMarketDto {

	/**
	 * 日期-忽略时间
	 */
	private Date date;

	/**
	 * 净投放量
	 */
	private BigDecimal netVolume;

	/**
	 * 投放量
	 */
	private BigDecimal openMarketVolume;

	/**
	 * 央票到期
	 */
	private BigDecimal centralTicketExpires;

	/**
	 * 正回购到期
	 */
	private BigDecimal repoMaturity;

	/**
	 * 逆回购操作
	 */
	private BigDecimal reverseRepurchaseOperation;

	/**
	 * 回笼量
	 */
	private BigDecimal withdrawalFromCirculation;

	/**
	 * 央票发行
	 */
	private BigDecimal centralIssueOfVotes;

	/**
	 * 正回购操作
	 */
	private BigDecimal positiveBuyBackOperation;

	/**
	 * 逆回购到期
	 */
	private BigDecimal reverseRepoMaturity;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public BigDecimal getNetVolume() {
		return netVolume;
	}

	public void setNetVolume(BigDecimal netVolume) {
		this.netVolume = netVolume;
	}

	public BigDecimal getOpenMarketVolume() {
		return openMarketVolume;
	}

	public void setOpenMarketVolume(BigDecimal openMarketVolume) {
		this.openMarketVolume = openMarketVolume;
	}

	public BigDecimal getCentralTicketExpires() {
		return centralTicketExpires;
	}

	public void setCentralTicketExpires(BigDecimal centralTicketExpires) {
		this.centralTicketExpires = centralTicketExpires;
	}

	public BigDecimal getRepoMaturity() {
		return repoMaturity;
	}

	public void setRepoMaturity(BigDecimal repoMaturity) {
		this.repoMaturity = repoMaturity;
	}

	public BigDecimal getReverseRepurchaseOperation() {
		return reverseRepurchaseOperation;
	}

	public void setReverseRepurchaseOperation(BigDecimal reverseRepurchaseOperation) {
		this.reverseRepurchaseOperation = reverseRepurchaseOperation;
	}

	public BigDecimal getWithdrawalFromCirculation() {
		return withdrawalFromCirculation;
	}

	public void setWithdrawalFromCirculation(BigDecimal withdrawalFromCirculation) {
		this.withdrawalFromCirculation = withdrawalFromCirculation;
	}

	public BigDecimal getCentralIssueOfVotes() {
		return centralIssueOfVotes;
	}

	public void setCentralIssueOfVotes(BigDecimal centralIssueOfVotes) {
		this.centralIssueOfVotes = centralIssueOfVotes;
	}

	public BigDecimal getPositiveBuyBackOperation() {
		return positiveBuyBackOperation;
	}

	public void setPositiveBuyBackOperation(BigDecimal positiveBuyBackOperation) {
		this.positiveBuyBackOperation = positiveBuyBackOperation;
	}

	public BigDecimal getReverseRepoMaturity() {
		return reverseRepoMaturity;
	}

	public void setReverseRepoMaturity(BigDecimal reverseRepoMaturity) {
		this.reverseRepoMaturity = reverseRepoMaturity;
	}
}
