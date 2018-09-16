package de.GaMoFu.RaidBosses.Items;

public enum ItemsEnum {

    //@formatter:off
	SHIELD_1(Shield1.class),
	SWORD_1(Sword1.class),
	
	STAFF_1(Staff1.class),
	STAFF_2(Staff2.class),
	STAFF_3(Staff3.class),
    
	TOKEN_1(Token1.class),
	TOKEN_2(Token2.class),
	TOKEN_3(Token3.class),
	TOKEN_4(Token4.class),
	TOKEN_5(Token5.class),
    TOKEN_6(Token6.class),
    TOKEN_7(Token7.class),
    TOKEN_8(Token8.class),
    
    EXCALIBUR(Excalibur.class);
	//@formatter:on

    private Class<? extends Item> itemClass;

    private ItemsEnum(Class<? extends Item> itemClass) {
        this.itemClass = itemClass;
    }

    public Class<? extends Item> getItemClass() {
        return itemClass;
    }
}
