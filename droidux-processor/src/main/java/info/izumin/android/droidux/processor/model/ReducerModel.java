package info.izumin.android.droidux.processor.model;

import com.squareup.javapoet.ClassName;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

import info.izumin.android.droidux.annotation.Dispatchable;
import info.izumin.android.droidux.annotation.Reducer;
import info.izumin.android.droidux.processor.util.StringUtils;

import static info.izumin.android.droidux.processor.util.AnnotationUtils.findMethodsByAnnotation;
import static info.izumin.android.droidux.processor.util.AnnotationUtils.getClassNameFromAnnotation;

/**
 * Created by izumin on 11/3/15.
 */
public class ReducerModel {
    public static final String TAG = ReducerModel.class.getSimpleName();

    private final TypeElement element;
    private final ClassName state;
    private final ClassName reducer;

    private final String qualifiedName;
    private final String packageName;
    private final String className;
    private final String variableName;
    private final String stateName;

    private final StoreModel storeModel;
    private final List<DispatchableModel> dispatchableModels;

    public ReducerModel(TypeElement element) {
        this.element = element;
        this.state = getClassNameFromAnnotation(element, Reducer.class, "value");

        this.qualifiedName = element.getQualifiedName().toString();
        this.packageName = StringUtils.getPackageName(qualifiedName);
        this.className = StringUtils.getClassName(qualifiedName);
        this.variableName = StringUtils.getLowerCamelFromUpperCamel(className);
        if (state != null) {
            this.stateName = state.simpleName();
        } else {
            throw new IllegalArgumentException();
        }

        this.reducer = ClassName.get(packageName, className);
        this.storeModel = new StoreModel(this);
        this.dispatchableModels = new ArrayList<>();
        for (ExecutableElement el : findMethodsByAnnotation(element, Dispatchable.class)) {
            dispatchableModels.add(new DispatchableModel(el));
        }
    }

    public TypeElement getElement() {
        return element;
    }

    public ClassName getState() {
        return state;
    }

    public ClassName getReducer() {
        return reducer;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    public String getVariableName() {
        return variableName;
    }

    public String getStateName() {
        return stateName;
    }

    public StoreModel getStoreModel() {
        return storeModel;
    }

    public List<DispatchableModel> getDispatchableModels() {
        return dispatchableModels;
    }
}