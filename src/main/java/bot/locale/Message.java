package bot.locale;

public enum Message{

    MSG_ERROR("message_error"),
    MSG_RESTART_REQUIRED("message_restart_required"),


    CMD_INVALID_ROLE("command_role_invalid"),
    CMD_INVALID_USER("command_user_invalid"),
    CMD_INVALID_CMD("command_command_invalid"),

    CMD_CLEAR_NOT_HERE("command_clear_nothere"),
    CMD_CLEAR_PROMPT_1("command_clear_1a"),
    CMD_CLEAR_CONF_1("command_clear_1b"),
    CMD_CLEAR_RESPONSE_1("command_clear_response_1"),
    CMD_CLEAR_PROMPT_2("command_clear_2a"),
    CMD_CLEAR_CONF_2("command_clear_2b"),
    CMD_CLEAR_RESPONSE_2("command_clear_response_2"),
    CMD_CLEAR_DELETING("command_clear_deleting"),
    CMD_CLEAR_DELETED("command_clear_deleted"),
    CMD_CLEAR_CANCELLED("command_clear_cancel"),

    CMD_FEATURE_LIST("command_feature_list"),
    CMD_FEATURE_ARGS("command_feature_args"),
    CMD_FEATURE_ENABLED("command_feature_enable"),
    CMD_FEATURE_DISABLED("command_feature_disable"),
    CMD_FEATURE_NO_DISABLE("command_feature_no_disable"),
    
    CMD_GAME_NO_HOME("command_game_no_home"),
    CMD_GAME_NOT_HERE("command_game_nothere"),
    CMD_GAME_CHOOSE_1("command_game_choose_1"),
    CMD_GAME_CHOOSE_2("command_game_choose_2"),
    CMD_GAME_CHOOSE_3("command_game_choose_3"),
    CMD_GAME_START("command_game_start"),
    CMD_GAME_THANKS("command_game_thanks"),

    CMD_HELP_DETAILS("command_help_details"),
    CMD_HELP_NO_DETAIL("command_help_no_detail"),

    CMD_MEME_SEND("command_meme_normal"),
    CMD_MEME_DANK("command_meme_dank"),

    CMD_PRUNE_DELETED("command_prune_deleted"),

    CMD_RABBIT_NOT_FOUND("command_rabbit_not_found"),

    CMD_RESTART_RESTARTING("command_restart_restarting"),
    CMD_RESTART_FINISHED("command_restart_finished"),

    CMD_SETTING_LIST("command_setting_list"),
    CMD_SETTING_MORE("command_setting_more"),
    CMD_SETTING_RESET("command_setting_reset"),
    CMD_SETTING_INVALID_SETTING("command_setting_invalid_setting"),
    CMD_SETTING_INVALID_VALUE("command_setting_invalid_value"),
    CMD_SETTING_NO_ACCESS("command_setting_no_access"),
    CMD_SETTING_ENTER_NUMBER("command_setting_enter_number"),
    CMD_SETTING_ENTER_NUMBER_SERVER("command_setting_enter_number_server"),
    CMD_SETTING_INVALID_NUMBER("command_setting_invalid_number"),
    CMD_SETTING_SET("command_setting_set"),

    CMD_SOUND_INVALID("command_sound_invalid"),
    CMD_SOUND_NOCHANNEL("command_sound_nochannel"),

    CMD_TEST_MESSAGE("command_test"),

    CMD_WAIFU_LIST("command_waifu_list"),
    CMD_WAIFU_ADD("command_waifu_add"),
    CMD_WAIFU_REMOVE("command_waifu_remove"),
    CMD_WAIFU_ALREADY_ON("command_waifu_already_on"),
    CMD_WAIFU_NOT_ON("command_waifu_not_on"),
    CMD_WAIFU_NOTIFY_ADD("command_waifu_notify_add"),
    CMD_WAIFU_NOTIFY_REMOVE("command_waifu_notify_remove"),


    FUNC_BREAK_INTRO("function_break_intro"),
    FUNC_BREAK_SUBMITTED("function_break_submitted"),
    FUNC_BREAK_FROM("function_break_from"),

    FUNC_EAT_MESSAGE("function_eat_message"),
    FUNCTION_EAT_MESSAGE_2("function_eat_message_drink"),

    FUNC_YTTIME_LENGTH("function_vidtime_length"),
    FUNC_YTTIME_BLOCKED("function_vidtime_blocked"),
    FUNC_YTTIME_UNBLOCK("function_vidtime_unblock"),

    FUNC_WELCOME_MESSAGE("function_welcome_message"),

    GAME_TIC_TAC_TOE("game_tictactoe"),
    GAME_SCRAMBLE("game_scramble");

    private final String name;

    Message(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public String getLocalizedMessage(Locale locale){
        return LocaleHandler.get(locale).getLocalizedMessage(this);
    }
}
