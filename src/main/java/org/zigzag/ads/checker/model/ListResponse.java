package org.zigzag.ads.checker.model;

import java.util.List;

public class ListResponse {
	private String status;
	private AdsData data;

	public static final class Ads {
		private int id;
		private boolean isPaid;
		private boolean isPaidUp;

		public int getId() {
			return id;
		}

		public boolean getIsPaid() {
			return isPaid;
		}

		public boolean getIsPaidUp() {
			return isPaidUp;
		}
	}

	public static final class AdsData {
		private List<Ads> list;
		private boolean isLastPage;

		public List<Ads> getList() {
			return list;
		}

		public boolean getIsLastPage() {
			return isLastPage;
		}
	}

	public String getStatus() {
		return status;
	}

	public AdsData getData() {
		return data;
	}
}
