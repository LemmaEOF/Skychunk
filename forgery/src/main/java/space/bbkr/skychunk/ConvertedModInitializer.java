package space.bbkr.skychunk;

public abstract class ConvertedModInitializer {

	public ConvertedModInitializer() {
		onInitialize();
	}
	
	public abstract void onInitialize();
	
}
