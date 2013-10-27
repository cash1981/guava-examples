package no.webstep.fagdag.guava;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.InputStream;

import org.junit.Test;

import com.google.common.io.Closer;
import com.google.common.net.HostAndPort;

public class IOTest {
	@Test(expected = NullPointerException.class)
	// require guava 14+
	public void tryWithResources() throws Exception {

		Closer closer = Closer.create();
		try {
			InputStream doesNotExist = closer.register(getInputStream());
			byte[] b = new byte[2];
			int read = doesNotExist.read(b, 0, 2);
		} catch (Exception e) {
			throw closer.rethrow(e);
		} finally {
			closer.close();
		}

	}

	private InputStream getInputStream() {
		return getClass().getResourceAsStream("doesNotExist");
	}

	@Test
	public void parseHostnameAndPort() throws Exception {
		HostAndPort hap = HostAndPort.fromString("localhost:80");

		assertThat(hap.getHostText(), is("localhost"));
		assertThat(hap.getPort(), is(80));
	}

	@Test
	public void parseipv6() throws Exception {
		HostAndPort hap = HostAndPort.fromString("[2001:db8::1]");

		assertThat(hap.getHostText(), is("2001:db8::1"));
		assertThat(hap.getPortOrDefault(8080), is(8080));

	}
}
