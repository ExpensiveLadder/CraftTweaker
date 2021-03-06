package crafttweaker;

/**
 * Defines an action.
 * <p>
 * Every modification made by a command must be registered to the Tweaker with
 * the Tweaker.apply method. This enables CraftTweaker to keep track of actions
 * that have been performed.
 * <p>
 * There also exist semi-permanent actions. Those actions can return an override
 * key; if a newer action has the same override key as an old action, the new
 * action is considered to override the old one.
 * <p>
 * Likewise, actions can implement hashCode and equals methods to indicate that
 * they are equal. If an action is equal to a non-undoable stuck action, it will
 * be omitted from execution.
 *
 * @author Stan Hebben
 */
public interface IAction {
    
    /**
     * Executes what the action is supposed to do.
     */
    void apply();
    
    
    /**
     * Describes, in a single human-readable sentence, what this specific action
     * is doing. Used in logging messages, lists, ...
     * <p>
     * Try to be as descriptive as possible without being too verbose.
     * <p>
     * Examples: - Adding Peach planks to the woodPlanks ore dictionary entry -
     * Removing a recipe for Iron Ore
     *
     * @return the description of this action
     */
    String describe();
    
    /**
     * Determines if an IAction can be applied. Reasons for why the action is invalid should be supplied in {@link #describeInvalid()}
     *
     * @return true if the IAction can be applied, false otherwise.
     */
    default boolean validate() {
        return true;
    }
    
    /**
     * If the action is invalid, this is the message the user gets told. Will only be called if {@link #validate()}
     *
     * @return message to tell the user why it is invalid.
     */
    default String describeInvalid() {
        return "Valid by default!";
    }
}
