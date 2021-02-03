package com.yao.spider.common.util;

import com.yao.spider.common.exception.ConvertException ;
import com.yao.spider.common.intf.Condition ;
import com.yao.spider.common.intf.Composer ;
import com.yao.spider.common.intf.IdInterface ;

import java.util.*;

public class CollectionsUtil {

	public static final Comparator<IdInterface<Long>> comparator = new Comparator<IdInterface<Long>>() {
		public int compare(IdInterface<Long> o1, IdInterface<Long> o2) {
			if (o1.getId() > o2.getId()) {
				return 1;
			} else if (o1.getId() < o2.getId()) {
				return -1;
			}
			return 0;
		}
	};

	public static final Comparator<IdInterface<Long>> descendComparator = new Comparator<IdInterface<Long>>() {
		public int compare(IdInterface<Long> o2, IdInterface<Long> o1) {
			if (o1.getId() > o2.getId()) {
				return 1;
			} else if (o1.getId() < o2.getId()) {
				return -1;
			}
			return 0;
		}
	};

	public static void sort(List<? extends IdInterface<Long>> collecions) {
		Collections.sort(collecions, comparator);
	}

	public static void descendSort(List<? extends IdInterface<Long>> collections) {
		Collections.sort(collections, descendComparator);
	}

	public static boolean isNullOrEmpty(Collection<?> list) {
		return list == null || list.isEmpty();
	}

	public static boolean isNullOrEmpty(Map map) {
		return map == null || map.isEmpty();
	}

	public static boolean isNotEmpty(Map map){
		return !isNullOrEmpty(map);
	}

	public static boolean isNotEmpty(Collection<?>  collection){
		return !isNullOrEmpty(collection);
	}

	public static String join(String split, Collection<?> list) {

		if (isNullOrEmpty(list)) {
			return "";
		}

		StringBuilder sb = new StringBuilder(list.size() * 20); // 预估
		for (Object object : list) {
			sb.append(String.valueOf(object)).append(split);
		}
		return sb.substring(0, sb.length() - split.length());
	}

	public static <V> List<V> subList(List<V> list, int startInclusive, int endExclusive){
		if (list == null) {
			throw new NullPointerException("cannot be null");
		}

		int lastIndexExclusive = (endExclusive > list.size() ? list.size() : endExclusive);
		return list.subList(startInclusive, lastIndexExclusive);
	}

	public static <K, V> String join(String split, Map<K, V> map) {
		if (map == null || map.isEmpty()) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		for (Map.Entry<K, V> entry : map.entrySet()) {
			sb.append(entry.getKey().toString()).append("=").append(entry.getValue().toString()).append(split);
		}
		return sb.substring(0, sb.length() - split.length());
	}

	public static Map<String, String> splitMap(String split, String str) {
		String[] strs = str.split(split);
		Map<String, String> map = new HashMap<String, String>(strs.length);
		for (String item : strs) {
			String[] items = item.split("=");
			if (items.length == 1) {
				map.put(items[0], "");
			} else if (items.length == 2) {
				map.put(items[0], items[1]);
			} else {
				throw new ConvertException("splitMap fail, array size more than 2 in one item, subStr:" + item + ", str:" + str);
			}
		}
		return map;
	}

	public static <E> boolean containDuplicateObject(Collection<E> list){
		int oldSize = list.size();
		return filterDuplicateObject(list).size() < oldSize;
	}

	public static <E> Collection<E> filterDuplicateObject(Collection<E> list){
		Set<E> set = new HashSet<E>();
		Iterator<E> it = list.iterator();
		while (it.hasNext()) {
			E e = (E) it.next();
			if (set.contains(e)){
				it.remove();
				continue;
			}

			set.add(e);
		}
		return list;
	}

	/**
	 * 根据Id来去重
	 */
	public static <E> Collection<? extends IdInterface<E>> filterDuplicates(Collection<? extends IdInterface<E>> collection) {
		Set<E> set = new HashSet<E>(collection.size());
		for (@SuppressWarnings("unchecked")
		Iterator<IdInterface<E>> iterator = (Iterator<IdInterface<E>>) collection.iterator(); iterator.hasNext();) {
			final IdInterface<E> idInterface = (IdInterface<E>) iterator.next();
			if (set.contains(idInterface.getId())) {
				iterator.remove();
			} else {
				set.add(idInterface.getId());
			}
		}
		return collection;
	}

	public static <E> List<E> getAllIds(Collection<? extends IdInterface<E>> collections) {
		List<E> list = new ArrayList<E>(collections.size());
		for (IdInterface<E> item : collections) {
			list.add(item.getId());
		}
		return list;
	}

	public static <V extends Comparable<V>, T> SortedMap<V, T> sortMapInKeyAscend(Map<V, T> rawParams) {
		SortedMap<V, T> map = new TreeMap<V, T>(SCEND_COMPARATOR);
		map.putAll(rawParams);
		return map;
	}

	private static Comparator<Comparable> SCEND_COMPARATOR = new Comparator<Comparable>() {
		public int compare(Comparable o1, Comparable o2) {
			return o1.compareTo(o2);
		}
	};

	public static List<String> convertToStringList(Collection<?> collections) {
		List<String> list = new ArrayList<String>(collections.size());
		Iterator<?> iter = collections.iterator();
		while (iter.hasNext()) {
			list.add(String.valueOf(iter.next()));
		}
		return list;
	}

    public static List<Long> convertToLongList(Collection<String> collections) {
        List<Long> list = new ArrayList<Long>(collections.size());
        Iterator<String> iter = collections.iterator();
        while (iter.hasNext()) {
            list.add(Long.valueOf(iter.next()));
        }
        return list;
    }

	public static <E, V> Map<E, V> mapComposerId(Collection<V> collection, Composer<E, V> c) {
		Map<E, V> map = new HashMap<E, V>(collection.size());
		for (Iterator<V> iterator = collection.iterator(); iterator.hasNext();) {
			V v = iterator.next();
			map.put(c.getComposerId(v), v);
		}

		return map;
	}

	public static <E, V> Map<E, List<V>> groupMapComposerId(Collection<V> collection, Composer<E, V> c) {
		Map<E, List<V>> map = new HashMap<E, List<V>>(collection.size());
		for (Iterator<V> iterator = collection.iterator(); iterator.hasNext();) {
			V v = iterator.next();
			E composerId = c.getComposerId(v);
			List<V> list = map.get(composerId);
			if(list == null){
				list = new LinkedList<V>();
				map.put(composerId, list);
			}
			list.add(v);
		}
		return map;
	}

	public static <E, V> List<E> getComposerIds(Collection<V> collection, Composer<E, V> c) {
		List<E> list = new LinkedList<E>();
		for (Iterator<V> iterator = collection.iterator(); iterator.hasNext();) {
			V v = iterator.next();
			list.add(c.getComposerId(v));
		}

		return list;
	}

	public static <E, V> List<E> getNoNullComposerIds(Collection<V> collection, Composer<E, V> c) {
		List<E> list = new LinkedList<E>();
		for (Iterator<V> iterator = collection.iterator(); iterator.hasNext();) {
			V v = iterator.next();
			if(v != null){
				E e = c.getComposerId(v);
				if(e != null){
					list.add(e);
				}
			}
		}

		return list;
	}

	public static <E, V> E getAndValidateUniqueComposerId(Collection<V> collection, Composer<E, V> c) {
		if (collection.isEmpty()) {
			return null;
		}

		E e = null;
		for (Iterator<V> iterator = collection.iterator(); iterator.hasNext();) {
			V v = iterator.next();
			if (e == null) {
				e = c.getComposerId(v);
			} else if (!e.equals(c.getComposerId(v))) {
				throw new ConvertException("duplicate ComposerId");
			}
		}
		return e;
	}

	public static <E, V> V getByComposerId(Collection<V> collection, E composerId, Composer<E, V> composer) {
		for (V v : collection) {
			if(composer.getComposerId(v) == null){
				if(composerId == null){
					return  v;
				}
			}else if(composer.getComposerId(v).equals(composerId)) {
				return v;
			}
		}
		return null;
	}

	public static <E, V> List<V> sortByComposerIds(Collection<V> rawValues, Collection<E> sortedKeys, Composer<E, V> c){
		Map<E, V> map = mapComposerId(rawValues, c);
		List<V> list = new LinkedList<V>();
		for(E e : sortedKeys){
			V v = map.remove(e);
			if(v!= null){
				//再做一次是为了保证不再sortedKeys中的数据也能保持原有的顺序
				rawValues.remove(v);
				list.add(v);
			}
		}
		for(V v : rawValues){
			list.add(v);
		}
		return list;
	}

	public static final <V> void removeAll(Collection<V> collection, V value){
		Iterator<V> iter = collection.iterator();
		for(;iter.hasNext();){
			V v = iter.next();
			if(value == null){
				if(v == null) {
					iter.remove();
				}
			}else if(value.equals(v)){
				iter.remove();
			}
		}
		return;
	}

	public static final <V> List<V> initOrAdd(List<V> collections, V value) {
		if (collections == null) {
			collections = new LinkedList<V>();
		}
		collections.add(value);
		return collections;
	}

	public static final <K, V> Map<K, V> initOrAdd(Map<K, V> map, K key, V value) {
		if (map == null) {
			map = new HashMap<K, V>();
		}
		map.put(key, value);
		return map;
	}

	public static final <K, V> Map<K, List<V>> initOrAddWithList(Map<K, List<V>> map, K key, V value) {
		if (map == null) {
			map = new HashMap<K, List<V>>();
		}
		map.put(key, initOrAdd(map.get(key), value));
		return map;
	}

	public static final <PK, SK, V> Map<PK, Map<SK, V>> initOrAddWithMap(Map<PK, Map<SK, V>> map, PK primaryKey, SK secondKey, V value) {
		if (map == null) {
			map = new HashMap<PK, Map<SK, V>>();
		}
		Map<SK, V> subMap = map.get(primaryKey);
		subMap = initOrAdd(subMap, secondKey, value);
		map = initOrAdd(map, primaryKey, subMap);
		return map;
	}

    public static final <PK, SK, V> Map<PK, Map<SK, List<V>>> initOrAddListWithMap(Map<PK, Map<SK, List<V>>> map, PK primaryKey, SK secondKey, V value) {
        if (map == null) {
            map = new HashMap<PK, Map<SK, List<V>>>();
        }

        Map<SK, List<V>> subMap = map.get(primaryKey);
        subMap = initOrAddWithList(subMap, secondKey, value);
        map = initOrAdd(map, primaryKey, subMap);
        return map;
    }

	public static final <PK, SK, V> Map<PK, List<SK>> composerIdsMap(List<V> list, Composer<PK, V> primaryComposerId, Composer<SK, V> secondComposerId){
		if (CollectionsUtil.isNullOrEmpty(list)) {
			return Collections.emptyMap();
		}

		Map map = new HashMap<PK, List<SK>>();
		for (V v : list) {
			map = initOrAddWithList(map, primaryComposerId.getComposerId(v), secondComposerId.getComposerId(v));
		}
		return map;
	}

    public static final <PK, SK, V> Map<PK, Map<SK, List<V>>> duplxComposerIdsMap(Collection<V> list, Composer<PK, V> primaryComposerId, Composer<SK, V> secondComposerId){
        if (CollectionsUtil.isNullOrEmpty(list)) {
            return Collections.emptyMap();
        }

        Map map = new HashMap<PK, Map<SK, List<V>>>();
        for (V v : list) {
            map = initOrAddListWithMap(map, primaryComposerId.getComposerId(v), secondComposerId.getComposerId(v), v);
        }
        return map;
    }

    public static final <K, V> Set<K> getComposerIdSet(List<V> list, Composer<K, V> composer) {

        if (CollectionsUtil.isNullOrEmpty(list)) {
            return Collections.emptySet();
        }

        Set set = new HashSet();
        for (V item : list) {
            set.add(composer.getComposerId(item));
        }

        return set;
    }

    public static final <PK, SK, V> Map<PK, SK> mapComposerIds(Collection<V> list, Composer<PK, V> primaryComposerId, Composer<SK, V> secondComposerId) {
        if (CollectionsUtil.isNullOrEmpty(list)) {
            return Collections.emptyMap();
        }

        Map map = new HashMap<PK, SK>();
        for (V v : list) {
            map.put(primaryComposerId.getComposerId(v), secondComposerId.getComposerId(v));
        }
        return map;
    }

    public static <T> T getFirstElement(Collection<T> list, Condition<T> condition, Comparator<T> comparator){
		if(isNullOrEmpty(list)){
			return null;
		}

		T firstElement = null;
		for(T t : list){
			if(condition.match(t) && comparator.compare(t, firstElement) < 0){
				firstElement = t;
			}
		}

		return firstElement;
	}


    public static void main(String[] args) {
		List<Long> list = null;

		list = initOrAdd(list, 3L);
		list = initOrAdd(list, 4L);
		System.out.println(list);

		Map<Long, List<Long>> map = null;
		map = initOrAddWithList(map, 1L, 2L);
		map = initOrAddWithList(map, 1L, 2L);
		map = initOrAddWithList(map, 2L, 4L);
		map = initOrAddWithList(map, 2L, 3L);
		System.out.println(map.toString());


		Map<Long, Map<Long, Long>> map2 = null;
		map2 = initOrAddWithMap(map2, 1L, 2L, 2L);
		map2 = initOrAddWithMap(map2, 1L, 2L, 1L);
		map2 = initOrAddWithMap(map2, 2L, 3L, 3L);
		map2 = initOrAddWithMap(map2, 2L, 3L, 3L);
		map2 = initOrAddWithMap(map2, 2L, 4L, 3L);
		System.out.println(map2.toString());


	}
}
