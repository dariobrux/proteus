/*
 * Copyright 2019 Flipkart Internet Pvt. Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.flipkart.android.proteus.value;

import com.flipkart.android.proteus.toolbox.LazilyParsedNumber;

import java.math.BigInteger;

/**
 * Primitive
 *
 * @author aditya.sharat
 */

public class Primitive extends Value
{

    private static final Class<?>[] PRIMITIVE_TYPES = {
            int.class,
            long.class,
            short.class,
            float.class,
            double.class,
            byte.class,
            boolean.class,
            char.class,
            Integer.class,
            Long.class,
            Short.class,
            Float.class,
            Double.class,
            Byte.class,
            Boolean.class,
            Character.class};

    private java.lang.Object value;

    /**
     * Create a primitive containing a boolean value.
     *
     * @param bool the value to create the primitive with.
     */
    public Primitive(Boolean bool)
    {
        setValue(bool);
    }

    /**
     * Create a primitive containing a {@link Number}.
     *
     * @param number the value to create the primitive with.
     */
    public Primitive(Number number)
    {
        setValue(number);
    }

    /**
     * Create a primitive containing a String value.
     *
     * @param string the value to create the primitive with.
     */
    public Primitive(String string)
    {
        setValue(string);
    }

    /**
     * Create a primitive containing a character. The character is turned into a one character String.
     *
     * @param c the value to create the primitive with.
     */
    public Primitive(Character c)
    {
        setValue(c);
    }

    /**
     * Create a primitive using the specified ObjectValue. It must be an instance of {@link Number}, a
     * Java primitive type, or a String.
     *
     * @param primitive the value to create the primitive with.
     */
    Primitive(java.lang.Object primitive)
    {
        setValue(primitive);
    }

    static boolean isPrimitiveOrString(java.lang.Object target)
    {
        if (target instanceof String)
        {
            return true;
        }

        Class<?> classOfPrimitive = target.getClass();
        for (Class<?> standardPrimitive : PRIMITIVE_TYPES)
        {
            if (standardPrimitive.isAssignableFrom(classOfPrimitive))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if the specified number is an integral type
     * (Long, Integer, Short, Byte, BigInteger)
     */
    private static boolean isIntegral(Primitive primitive)
    {
        if (primitive.value instanceof Number)
        {
            Number number = (Number) primitive.value;
            return number instanceof BigInteger || number instanceof Long || number instanceof Integer || number instanceof Short || number instanceof Byte;
        }
        return false;
    }

    @Override
    public Primitive copy()
    {
        return this;
    }

    void setValue(java.lang.Object primitive)
    {
        if (primitive instanceof Character)
        {
            char c = (Character) primitive;
            this.value = String.valueOf(c);
        }
        else
        {
            if (!(primitive instanceof Number || isPrimitiveOrString(primitive)))
            {
                throw new IllegalArgumentException();
            }
            this.value = primitive;
        }
    }

    Object getValue() {
        return value;
    }

    /**
     * Check whether this primitive contains a boolean value.
     *
     * @return true if this primitive contains a boolean value, false otherwise.
     */
    public boolean isBoolean()
    {
        return value instanceof Boolean;
    }

    /**
     * convenience method to get this value as a {@link Boolean}.
     *
     * @return get this value as a {@link Boolean}.
     */
    Boolean getAsBooleanWrapper()
    {
        return (Boolean) value;
    }

    /**
     * convenience method to get this value as a boolean value.
     *
     * @return get this value as a primitive boolean value.
     */
    @Override
    public boolean getAsBoolean()
    {
        if (isBoolean())
        {
            return getAsBooleanWrapper();
        }
        else
        {
            // Check to see if the value as a String is "true" in any case.
            return Boolean.parseBoolean(getAsString());
        }
    }

    /**
     * Check whether this primitive contains a Number.
     *
     * @return true if this primitive contains a Number, false otherwise.
     */
    public boolean isNumber()
    {
        return value instanceof Number;
    }

    /**
     * convenience method to get this value as a Number.
     *
     * @return get this value as a Number.
     * @throws NumberFormatException if the value contained is not a valid Number.
     */
    public Number getAsNumber()
    {
        return value instanceof String ? new LazilyParsedNumber((String) value) : (Number) value;
    }

    /**
     * Check whether this primitive contains a String value.
     *
     * @return true if this primitive contains a String value, false otherwise.
     */
    public boolean isString()
    {
        return value instanceof String;
    }

    /**
     * convenience method to get this value as a String.
     *
     * @return get this value as a String.
     */
    @Override
    public String getAsString()
    {
        if (isNumber())
        {
            return getAsNumber().toString();
        }
        else if (isBoolean())
        {
            return getAsBooleanWrapper().toString();
        }
        else
        {
            return (String) value;
        }
    }

    /**
     * convenience method to get this value as a primitive double.
     *
     * @return get this value as a primitive double.
     * @throws NumberFormatException if the value contained is not a valid double.
     */
    @Override
    public double getAsDouble()
    {
        return isNumber() ? getAsNumber().doubleValue() : Double.parseDouble(getAsString());
    }

    /**
     * convenience method to get this value as a float.
     *
     * @return get this value as a float.
     * @throws NumberFormatException if the value contained is not a valid float.
     */
    @Override
    public float getAsFloat()
    {
        return isNumber() ? getAsNumber().floatValue() : Float.parseFloat(getAsString());
    }

    /**
     * convenience method to get this value as a primitive long.
     *
     * @return get this value as a primitive long.
     * @throws NumberFormatException if the value contained is not a valid long.
     */
    @Override
    public long getAsLong()
    {
        return isNumber() ? getAsNumber().longValue() : Long.parseLong(getAsString());
    }

    /**
     * convenience method to get this value as a primitive integer.
     *
     * @return get this value as a primitive integer.
     * @throws NumberFormatException if the value contained is not a valid integer.
     */
    @Override
    public int getAsInt()
    {
        return isNumber() ? getAsNumber().intValue() : Integer.parseInt(getAsString());
    }

    @Override
    public char getAsCharacter()
    {
        return getAsString().charAt(0);
    }

    @Override
    public int hashCode()
    {
        if (value == null)
        {
            return 31;
        }
        // Using recommended hashing algorithm from Effective Java for longs and doubles
        if (isIntegral(this))
        {
            long value = getAsNumber().longValue();
            return (int) (value ^ (value >>> 32));
        }
        if (value instanceof Number)
        {
            long value = Double.doubleToLongBits(getAsNumber().doubleValue());
            return (int) (value ^ (value >>> 32));
        }
        return value.hashCode();
    }

    @Override
    public boolean equals(java.lang.Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null || getClass() != obj.getClass())
        {
            return false;
        }
        Primitive other = (Primitive) obj;
        if (value == null)
        {
            return other.value == null;
        }
        if (isIntegral(this) && isIntegral(other))
        {
            return getAsNumber().longValue() == other.getAsNumber().longValue();
        }
        if (value instanceof Number && other.value instanceof Number)
        {
            double a = getAsNumber().doubleValue();
            // Java standard types other than double return true for two NaN. So, need
            // special handling for double.
            double b = other.getAsNumber().doubleValue();
            return a == b || (Double.isNaN(a) && Double.isNaN(b));
        }
        return value.equals(other.value);
    }

    @Override
    public String toString()
    {
        return getAsString();
    }

    public String getAsSingleQuotedString()
    {
        return '\'' + getAsString() + '\'';
    }

    public String getAsDoubleQuotedString()
    {
        return '\"' + getAsString() + '\"';
    }
}
