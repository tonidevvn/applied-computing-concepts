package ca.uwindsor.appliedcomputing.final_project.data_structure;


public interface HashFamily<AnyType>
{
    int hash( AnyType x, int which );
    int getNumberOfFunctions( );
    void generateNewFunctions( );
}
