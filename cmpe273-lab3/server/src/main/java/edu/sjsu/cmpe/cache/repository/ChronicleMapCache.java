package edu.sjsu.cmpe.cache.repository;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import net.openhft.chronicle.map.ChronicleMapBuilder;
import edu.sjsu.cmpe.cache.domain.Entry;

public class ChronicleMapCache implements CacheInterface{
	private static String temp = System.getProperty("java.io.tmpdir");
    private static String pathName = temp + "/chronicalHashMapTxt.txt";
	private ConcurrentMap<Long, Entry> concurrentMap;
	private File file;
	
	public ChronicleMapCache() {
		ChronicleMapBuilder<Long, Entry> builder = ChronicleMapBuilder.of(Long.class, Entry.class).entries(100);
		file = new File(pathName);
		try{
			concurrentMap = builder.createPersistedTo(file);
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	@Override
	public Entry save(Entry newEntry) {
		checkNotNull(newEntry, "newEntry instance must not be null");
		concurrentMap.put(newEntry.getKey(), newEntry);
		return newEntry;
	}

	@Override
	public Entry get(Long key) {
		checkArgument(key > 0, "Key was %s but expected greater than zero value", key);
		return concurrentMap.get(key);
	}

	@Override
	public List<Entry> getAll() {
		return new ArrayList<Entry>(concurrentMap.values());
	}
}
