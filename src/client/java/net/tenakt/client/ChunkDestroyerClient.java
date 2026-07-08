package net.tenakt.client;

import net.fabricmc.api.ClientModInitializer;
import net.tenakt.MyModInitializer;

public class ChunkDestroyerClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		System.out.println(MyModInitializer.CONFIG.name());
	}
}