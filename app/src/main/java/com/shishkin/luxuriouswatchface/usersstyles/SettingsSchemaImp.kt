package com.shishkin.luxuriouswatchface.usersstyles

//class SettingsSchemaImp @Inject constructor() {

//    private val allSettings = HashMap<String, UserStyleSettingDescription<*>>()

//    private var editorSession: EditorSession? = null

//    var backgroundColor: UserStyleSetting.LongRangeUserStyleSetting? = editorSession.userStyleSchema[BACKGROUND_COLOR] as UserStyleSetting.LongRangeUserStyleSetting

//    private val backgroundColor = UserStyleSettingDescription(
//        UserSettings::backGroundColor
//    ) {
//        UserStyleSettingDescription.AdditionalDescription(
//            R.string.setting_background_color_name,
//            R.string.setting_background_color_description
//        )
//    }

//    init{
//        allSettings[backgroundColor.id.value] = backgroundColor
//    }

//    private fun <V> createSetting(
//        id: String,
//        descriptionProducer: () -> UserStyleSettingWrapper.UserStyleSettingDescription<V>,
//    ) =
//        UserStyleSettingWrapper(
////            editorSession,
//            UserStyleSetting.Id(id),
//            descriptionProducer
//        )

//    private fun extractsUserStyles(userStyleSchema: UserStyleSchema) {
//        for (setting in userStyleSchema.rootUserStyleSettings) {
//            when (setting.id) {
//            }
//        }
//    }

//    fun createUserStyleSchema(resources: Resources) =
//        UserStyleSchema(
//            listOf(backgroundColor.create(resources))
//        )

//    fun initEditorSession(activity: AppCompatActivity){
//        activity.lifecycleScope.launch{
//            editorSession = EditorSession.createOnWatchEditorSession(
//                activity = activity
//            )
//        }
//    }
//
//    fun get(id: String) = editorSession?.userStyleSchema?.get(UserStyleSetting.Id(id))!!
//
//    fun set(id: String, userStyleOption: UserStyleSetting.Option){
//        val mutableUserStyle = editorSession.userStyle.value.toMutableUserStyle()
//        mutableUserStyle[get(id)] = userStyleOption
//        editorSession.userStyle.value = mutableUserStyle.toUserStyle()
//    }
//
//    fun <V> set(id: String, value: V){
//        set(id, toOption(value))
//    }
//
//    private fun <V> toOption(value: V) : UserStyleSetting.Option =
//        when (value) {
//            is Int -> UserStyleSetting.LongRangeUserStyleSetting.LongRangeOption(value.toLong())
//            else -> throw IllegalArgumentException("Unsupported ${javaClass.name} toOption type")
//        }

//    @Suppress("UNCHECKED_CAST")
//    fun get(setting: UserStyleSettingWrapper<*>) = editorSession.userStyleSchema[setting.id]!!

//    fun set(setting: UserStyleSettingWrapper<*>, option: UserStyleSetting.Option){
//        val mutableUserStyle = editorSession.userStyle.value.toMutableUserStyle()
//        mutableUserStyle[get(setting)] = option
//        editorSession.userStyle.value = mutableUserStyle.toUserStyle()
//    }



//}