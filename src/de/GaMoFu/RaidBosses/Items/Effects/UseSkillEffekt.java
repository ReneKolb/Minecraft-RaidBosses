package de.GaMoFu.RaidBosses.Items.Effects;

import org.bukkit.entity.Player;

import de.GaMoFu.RaidBosses.PlayerSettings;
import de.GaMoFu.RaidBosses.RaidBosses;
import de.GaMoFu.RaidBosses.Skill.ISkill;

public class UseSkillEffekt extends ItemEffect {

    private ISkill skill;

    public UseSkillEffekt(ISkill skill) {
        this.skill = skill;
    }

    public String getTootipText() {
        return "Right-Click: use " + skill.getSkillDisplayName();
    }

    @Override
    public void onInteract(Player player) {
        PlayerSettings ps = RaidBosses.getPluginInstance().getPlayerSettings(player);
        ps.handleUseSkill(skill);
    }

    @Override
    public String getUniqueEffectID() {
        return "aa515e06-f026-47e4-8121-1e56464f1d6a_" + skill.getSkillInternalName();
    }

}
