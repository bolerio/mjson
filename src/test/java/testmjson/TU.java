package testmjson;

import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by biordanov on 11/7/2014.
 */
public class TU
{
    public static <E> Set<E> set(Collection<E> C)
    {
        HashSet<E> S = new HashSet<E>();
        S.addAll(C);
        return S;
    }
    
	public static URL resource(String location)
	{
		return TU.class.getResource(location);
	}   
}