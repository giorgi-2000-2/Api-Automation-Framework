package ge.gmikeladze.platzi.unit;

import ge.gmikeladze.platzi.cleanup.CleanupRegistry;
import ge.gmikeladze.platzi.cleanup.ResourceKey;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class CleanupRegistryTest {

    private FakeReporter reporter;
    private CleanupRegistry registry;

    @BeforeMethod
    public void setUp() {
        reporter = new FakeReporter();
        registry = new CleanupRegistry(reporter);
    }

    @Test
    public void register_thenCleanup_runsAction() {
        final AtomicBoolean called = new AtomicBoolean(false);
        ResourceKey key = new ResourceKey(ResourceKey.TYPE_PRODUCT, 10);

        registry.register(key, new Runnable() {
            @Override
            public void run() {
                called.set(true);
            }
        });
        registry.cleanup();

        Assert.assertTrue(called.get(), "cleanup-მა უნდა გამოიძახოს დარეგისტრირებული action");
    }

    @Test
    public void cleanup_runsActionsInLIFOOrder() {
        final List<String> order = new ArrayList<String>();

        registry.register(new ResourceKey(ResourceKey.TYPE_CATEGORY, 1), new Runnable() {
            @Override
            public void run() {
                order.add("category-1");
            }
        });
        registry.register(new ResourceKey(ResourceKey.TYPE_PRODUCT, 2), new Runnable() {
            @Override
            public void run() {
                order.add("product-2");
            }
        });
        registry.register(new ResourceKey(ResourceKey.TYPE_USER, 3), new Runnable() {
            @Override
            public void run() {
                order.add("user-3");
            }
        });

        registry.cleanup();

        Assert.assertEquals(order, Arrays.asList("user-3", "product-2", "category-1"));
    }

    @Test
    public void markCompleted_skipsActionOnCleanup() {
        final AtomicBoolean called = new AtomicBoolean(false);
        ResourceKey key = new ResourceKey(ResourceKey.TYPE_PRODUCT, 5);

        registry.register(key, new Runnable() {
            @Override
            public void run() {
                called.set(true);
            }
        });
        registry.markCompleted(key);
        registry.cleanup();

        Assert.assertFalse(called.get(), "markCompleted-ის შემდეგ action აღარ უნდა შესრულდეს");
    }

    @Test
    public void markCompleted_onlyAffectsMatchingKey() {
        final AtomicBoolean productCalled = new AtomicBoolean(false);
        final AtomicBoolean categoryCalled = new AtomicBoolean(false);

        ResourceKey productKey = new ResourceKey(ResourceKey.TYPE_PRODUCT, 1);
        ResourceKey categoryKey = new ResourceKey(ResourceKey.TYPE_CATEGORY, 2);

        registry.register(productKey, new Runnable() {
            @Override
            public void run() {
                productCalled.set(true);
            }
        });
        registry.register(categoryKey, new Runnable() {
            @Override
            public void run() {
                categoryCalled.set(true);
            }
        });

        registry.markCompleted(productKey);
        registry.cleanup();

        Assert.assertFalse(productCalled.get());
        Assert.assertTrue(categoryCalled.get());
    }

    @Test
    public void duplicateRegister_isIgnored() {
        final AtomicInteger calls = new AtomicInteger(0);
        ResourceKey key = new ResourceKey(ResourceKey.TYPE_USER, 7);

        registry.register(key, new Runnable() {
            @Override
            public void run() {
                calls.incrementAndGet();
            }
        });
        registry.register(key, new Runnable() {
            @Override
            public void run() {
                calls.addAndGet(100);
            }
        });

        registry.cleanup();

        Assert.assertEquals(calls.get(), 1, "ერთი და იგივე key მხოლოდ ერთხელ უნდა დარეგისტრირდეს");
    }

    @Test
    public void cleanup_whenActionThrows_logsWarningAndContinues() {
        final List<String> order = new ArrayList<String>();

        registry.register(new ResourceKey(ResourceKey.TYPE_CATEGORY, 1), new Runnable() {
            @Override
            public void run() {
                order.add("first");
            }
        });
        registry.register(new ResourceKey(ResourceKey.TYPE_PRODUCT, 2), new Runnable() {
            @Override
            public void run() {
                throw new RuntimeException("simulated delete failure");
            }
        });
        registry.register(new ResourceKey(ResourceKey.TYPE_USER, 3), new Runnable() {
            @Override
            public void run() {
                order.add("third");
            }
        });

        registry.cleanup();

        Assert.assertEquals(order, Arrays.asList("third", "first"),
                "exception-ის შემდეგაც დანარჩენი action-ები უნდა შესრულდეს");
        Assert.assertTrue(reporter.hasWarningContaining("simulated delete failure"),
                "WARNING ლოგი უნდა ჩაიწეროს");
    }

    @Test
    public void cleanup_onEmptyRegistry_doesNothing() {
        registry.cleanup();
        Assert.assertTrue(reporter.getLogs().isEmpty());
    }

    @Test
    public void cleanup_canBeCalledMultipleTimesSafely() {
        final AtomicInteger calls = new AtomicInteger(0);
        ResourceKey key = new ResourceKey(ResourceKey.TYPE_PRODUCT, 99);

        registry.register(key, new Runnable() {
            @Override
            public void run() {
                calls.incrementAndGet();
            }
        });
        registry.cleanup();
        registry.cleanup();

        Assert.assertEquals(calls.get(), 1);
    }

    @Test
    public void markCompleted_onUnknownKey_isSafe() {
        registry.markCompleted(new ResourceKey(ResourceKey.TYPE_PRODUCT, 999));
        registry.cleanup();
        Assert.assertTrue(reporter.getLogs().isEmpty());
    }
}