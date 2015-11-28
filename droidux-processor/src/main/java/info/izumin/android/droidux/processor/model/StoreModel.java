package info.izumin.android.droidux.processor.model;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.squareup.javapoet.ClassName;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import info.izumin.android.droidux.annotation.Store;

import static com.google.auto.common.MoreTypes.asTypeElement;
import static info.izumin.android.droidux.processor.util.AnnotationUtils.getTypesFromAnnotation;

/**
 * Created by izumin on 11/3/15.
 */
public class StoreModel {
    public static final String TAG = StoreModel.class.getSimpleName();

    private static final String CLASS_NAME_PREFIX = "Droidux";
    private static final String BUILDER_CLASS_NAME = "Builder";

    private final TypeElement element;

    private final ClassName interfaceName;
    private final ClassName className;

    private final List<ReducerModel> reducerModels;
    private final List<StoreImplModel> storeImplModels;
    private final List<StoreMethodModel> methodModels;
    private final BuilderModel builderModel;

    public StoreModel(TypeElement element) {
        this.element = element;
        this.interfaceName = ClassName.get(element);
        this.className = ClassName.get(interfaceName.packageName(), CLASS_NAME_PREFIX + interfaceName.simpleName());
        reducerModels = FluentIterable.from(getTypesFromAnnotation(element, Store.class, "value"))
                .transform(new Function<TypeMirror, ReducerModel>() {
                    @Override
                    public ReducerModel apply(TypeMirror input) {
                        return new ReducerModel(asTypeElement(input));
                    }
                }).toList();

        storeImplModels = FluentIterable.from(reducerModels)
                .transform(new Function<ReducerModel, StoreImplModel>() {
                    @Override
                    public StoreImplModel apply(ReducerModel input) {
                        return new StoreImplModel(StoreModel.this, input);
                    }
                }).toList();

        methodModels = FluentIterable.from(element.getEnclosedElements())
                .filter(ExecutableElement.class)
                .transform(new Function<ExecutableElement, StoreMethodModel>() {
                    @Override
                    public StoreMethodModel apply(ExecutableElement input) {
                        return new StoreMethodModel(input, StoreModel.this);
                    }
                }).toList();

        this.builderModel = new BuilderModel(this);

    }

    public ClassName getInterfaceName() {
        return interfaceName;
    }

    public ClassName getClassName() {
        return className;
    }

    public List<ReducerModel> getReducerModels() {
        return reducerModels;
    }

    public List<StoreImplModel> getStoreImplModels() {
        return storeImplModels;
    }

    public List<StoreMethodModel> getMethodModels() {
        return methodModels;
    }

    public BuilderModel getBuilderModel() {
        return builderModel;
    }
}
