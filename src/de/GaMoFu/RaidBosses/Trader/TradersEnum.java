package de.GaMoFu.RaidBosses.Trader;

public enum TradersEnum {

	//@formatter:off
	WATER_MAGE_TRADER(WaterMageTrader1.class);
	//@formatter:on

	private Class<? extends Trader> skillClass;

	private TradersEnum(Class<? extends Trader> skillClass) {
		this.skillClass = skillClass;
	}

	public Class<? extends Trader> getTraderClass() {
		return skillClass;
	}

}
