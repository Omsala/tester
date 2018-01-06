# Code Standard

Underscore: Oskar, Wentin
Camel: 

One line functions/calls

# Erlang



Code header:
```
%% ------------------------
%% @title Name Of This File
%% @version 1.0.0
%% {@date}
%% @author Arthur O'disfile
%% @doc Description of this file and a quick rundown of what the
%% user will use it for. 
%% @end
%% ------------------------
```

Documentation and specification:
```
%% @doc A thorough description of what the function accomplishes,
%% and why it exists. With descpritions of what A, B and C are 
%% and what the function returns.
%% === Example ===
%% <div class="example">```
%% > example:function(A, B, C).
%% return_value'''
%% </div>
-spec function(A, B, C) -> type_of_result() when
      A::type_of_A(),
      B::type_of_B(),
      C::type_of_C().
```
Documentation is important for every function we write. They are not necessary for each pattern matching function call (see the recusrive functions example), but is necessary for auxiliary functions (see the auxiliary functions example below).

The documentation consists of two parts:
    The first part is the `@doc` and examples part, where we try to concisely describe what the function accomplishes. It is not too important HOW it does what it does, but mostly what we expect to use it for. 
    The second part is the `-spec` part, where we write out the function with pedagogical names of the variables (should be the same as what is written in the actual code, but can be changed for extra clarity), and specify what type they are. In the case that a function does not return a result, use the atom `ok`.

The two sections of documentation are not seperated by a new line, but the two sections of documentation and the function should be seperated by two empty lines. The documentation as well as the function should be encased in ´%%-----%%´ borders. 

Auxiliary functions:
```
%% Documentation

function(A, B, C) ->
      return_value.

%% Documentation

function(A, B, C, D) ->
      return_value.
```
Auxiliary functions are neccesary when doing tail recursion and simplifying the experience for the end user. in these cases the original function and the auxiliary function will have the same name, where the aux function is placed after the original. The two functions must be seperated by documentation as they are two different functions. 


Recursive functions:
```
%% A sentence describing the final case
recursive_function(0, 0, 0) ->
      return_value;

%% A sentence describing what case this pattern matching catches
recursive_function(0, B, C) ->
      recursive_function(A, B, C);

%% A sentence describing the first call
recursive_function(A, B, C) ->      recursive_function(A, B, C).

```



# Java






# Make