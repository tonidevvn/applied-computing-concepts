'use client'
import { Flex} from "antd";
import AppAutoComplete from "@/app/components/AppAutoComplete";
import AppSpellChecking from "@/app/components/AppSpellChecking";
import React, {useState} from "react";
import {useDebounce} from "use-debounce";

export default function Page() {

    const [searchValue, setSearchValue] = useState('')
    const [debounced ] = useDebounce(searchValue, 1000);

    return (
        <Flex vertical={true} gap={'middle'}>
            <h2>Autocomplete & Spell Checking</h2>
            <Flex vertical={false} gap={'middle'} >
                <AppAutoComplete
                    searchValue={searchValue}
                    setSearchValue={setSearchValue}
                    placeholder={'Search food items'}
                />
            </Flex>
            <AppSpellChecking
                searchValue={debounced}
                setSearchValue={setSearchValue}
            />
        </Flex>
    )
}