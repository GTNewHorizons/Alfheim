package alfheim;

public interface DevInfo {
	
	public static final String[] fixes = {
		"Включение/блокировка всего ЕСМ/ММО функционала при выполнении команд включения/отключения ЕСМ/ММО",
		// А именно:
		// "Добавление/удаление записей в лексиконе",
		// "Изменение их типа знаний",
		// "Включение/выключение гуи" -  ClientProxy
		// "Включение/выключение кейбиндов" -  ClientProxy
		// "Загрузка информации про точки респавна",
		// "Загрузка данных Кардинала"
	};
	
	public static final String[] todo = {
		"Партиклы из OSM",
			
		// Магия:
		"Fire Spear",
		"Ice Arrows",
		"Ice Tornado",
//		"Metamorphosis - интеграция с Morph",	// когда-нибудь потом....
//		"Peeping - интеграция с PiP",			// вообще вряд ли
		"Vacuum Blades",
		"Wind Needles"
	};
}
