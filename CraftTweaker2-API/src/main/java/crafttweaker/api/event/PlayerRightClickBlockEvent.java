package crafttweaker.api.event;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.world.IVector3d;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenSetter;

@ZenClass("crafttweaker.event.PlayerInteractBlockEvent")
@ZenRegister
public interface PlayerRightClickBlockEvent extends PlayerInteractEvent, IHasCancellationResult, IEventCancelable {

    @ZenGetter("hitVector")
    IVector3d getHitVector();

    @ZenGetter("useBlock")
    String getUseBlock();

    @ZenSetter("useBlock")
    void setUseBlock(String useBlock);

    @ZenGetter("useItem")
    String getUseItem();

    @ZenSetter("useItem")
    void setUseItem(String useItem);
}
