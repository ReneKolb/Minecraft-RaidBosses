package de.GaMoFu.RaidBosses.Items;

public enum ItemsEnum {

    //@formatter:off
	EXCALIBUR(Excalibur.class),
	
	TOKEN_1(Token1.class),
	TOKEN_2(Token2.class),
	TOKEN_3(Token3.class),
	TOKEN_4(Token4.class),
	TOKEN_5(Token5.class),
    TOKEN_6(Token6.class),
    TOKEN_7(Token7.class),
    TOKEN_8(Token8.class);
	//@formatter:on

    private Class<? extends IItem> itemClass;

    private ItemsEnum(Class<? extends IItem> itemClass) {
        this.itemClass = itemClass;
    }

    public Class<? extends IItem> getItemClass() {
        return itemClass;
    }
}
