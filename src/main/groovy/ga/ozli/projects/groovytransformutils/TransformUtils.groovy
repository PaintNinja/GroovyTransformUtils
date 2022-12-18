package ga.ozli.projects.groovytransformutils

import groovy.transform.CompileStatic
import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import groovyjarjarantlr4.v4.runtime.misc.Nullable
import org.apache.groovy.ast.tools.AnnotatedNodeUtils
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.FieldNode
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.Parameter
import org.codehaus.groovy.ast.expr.Expression
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.ast.stmt.Statement

import static org.objectweb.asm.Opcodes.ACC_PRIVATE
import static org.objectweb.asm.Opcodes.ACC_STATIC
import static org.objectweb.asm.Opcodes.ACC_FINAL
import static org.objectweb.asm.Opcodes.ACC_PUBLIC

@CompileStatic
class TransformUtils {
    static final int CONSTANT_MODIFIERS = ACC_PRIVATE | ACC_STATIC | ACC_FINAL

    @NamedVariant
    static FieldNode addField(@NamedParam(required = true) final ClassNode targetClassNode,
                              @NamedParam(required = true) final String fieldName,
                              @NamedParam final int modifiers = ACC_PUBLIC, // note: public fields aren't always properties
                              @NamedParam final ClassNode type = ClassHelper.DYNAMIC_TYPE,
                              @NamedParam @Nullable final List<AnnotationNode> annotations = null,
                              @NamedParam @Nullable final Expression initialValue = null,
                              @NamedParam final boolean addFieldFirst = false) {

        final FieldNode field = addFieldFirst ? targetClassNode.addFieldFirst(fieldName, modifiers, type, initialValue)
                                              : targetClassNode.addField(fieldName, modifiers, type, initialValue)

        if (annotations) field.addAnnotations(annotations)
        AnnotatedNodeUtils.markAsGenerated(targetClassNode, field)

        return field
    }

    @NamedVariant
    static FieldNode addStaticField(@NamedParam(required = true) final ClassNode targetClassNode,
                                    @NamedParam(required = true) final String fieldName,
                                    @NamedParam final int modifiers = ACC_PUBLIC | ACC_STATIC, // note: public fields aren't always properties
                                    @NamedParam final ClassNode type = ClassHelper.DYNAMIC_TYPE,
                                    @NamedParam @Nullable final List<AnnotationNode> annotations = null,
                                    @NamedParam @Nullable final Expression initialValue = null,
                                    @NamedParam final boolean addFieldFirst = false) {
        return addField(targetClassNode, fieldName, (modifiers | ACC_STATIC), type, annotations, initialValue, addFieldFirst)
    }

    @NamedVariant
    static FieldNode addConstant(@NamedParam(required = true) final ClassNode targetClassNode,
                                 @NamedParam(required = true) final String fieldName,
                                 @NamedParam final int modifiers = CONSTANT_MODIFIERS,
                                 @NamedParam final ClassNode type = ClassHelper.DYNAMIC_TYPE,
                                 @NamedParam @Nullable final List<AnnotationNode> annotations = null,
                                 @NamedParam @Nullable final Expression initialValue = null,
                                 @NamedParam final boolean addFieldFirst = true) {
        return addField(targetClassNode, fieldName, modifiers, type, annotations, initialValue, addFieldFirst)
    }

    // todo: addProperty()

    @NamedVariant
    static MethodNode addMethod(@NamedParam(required = true) final ClassNode targetClassNode,
                                @NamedParam(required = true) final String methodName,
                                @NamedParam final int modifiers = ACC_PUBLIC,
                                @NamedParam final ClassNode returnType = ClassHelper.VOID_TYPE,
                                @NamedParam final Parameter[] parameters = Parameter.EMPTY_ARRAY,
                                @NamedParam final ClassNode[] exceptions = ClassNode.EMPTY_ARRAY,
                                @NamedParam @Nullable final List<AnnotationNode> annotations = null,
                                @NamedParam @Nullable final Statement code = new BlockStatement()) {
        final MethodNode method = targetClassNode.addMethod(methodName, modifiers, returnType, null, null, code)

        if (annotations) method.addAnnotations(annotations)
        AnnotatedNodeUtils.markAsGenerated(targetClassNode, method)

        return method
    }

    @NamedVariant
    static MethodNode addStaticMethod(@NamedParam(required = true) final ClassNode targetClassNode,
                                      @NamedParam(required = true) final String methodName,
                                      @NamedParam final int modifiers = ACC_PUBLIC,
                                      @NamedParam final ClassNode returnType = ClassHelper.VOID_TYPE,
                                      @NamedParam final Parameter[] parameters = Parameter.EMPTY_ARRAY,
                                      @NamedParam final ClassNode[] exceptions = ClassNode.EMPTY_ARRAY,
                                      @NamedParam @Nullable final List<AnnotationNode> annotations = null,
                                      @NamedParam @Nullable final Statement code = new BlockStatement()) {
        return addMethod(targetClassNode, methodName, (modifiers | ACC_STATIC), returnType, parameters, exceptions, annotations, code)
    }
}
