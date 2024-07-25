import { AutoComplete } from 'antd'
import axios from 'axios'
import { useEffect, useState } from 'react'
import { useDebounce } from 'use-debounce'
import {isEmpty} from "@rc-component/mini-decimal/es/numberUtil";

function SearchValidation({searchValue}:{searchValue: string}) {
    //const [searchValidation, setSearchValidation] = useState("")
    let parsedRes = "";
    const priceQuery = searchValue.split(/\s+/).filter((w) => w.match("price:")).join(" ")
    const query = searchValue.split(/\s+/).filter((w) => !w.match("price:")).join(" ")
    const regex = /^price:((\d*\.\d+|\d+)?-(\d*\.\d+|\d+)?)$/g;
    const ms = priceQuery.matchAll(regex);
    // @ts-ignore
    for (const m of ms) {
        console.log(m);
        if (m[4]) {
            parsedRes = `price = ${m[4]}`;
        } else if (m[2] || m[3]) {
            const p1 = m[2] ? `price >= ${m[2]}` : null;
            const p2 = m[3] ? `price <= ${m[3]}`: null;
            if (p1 && p2) {
                if (m[2] <= m[3]) {
                    parsedRes = `${m[2]} ≤ price ≤ ${m[3]}`;
                } else {
                    parsedRes = "Invalid price query"
                }
            } else if (p1) {
                parsedRes = `price ≥ ${m[2]}`;
            } else if (p2) {
                parsedRes = `price ≤ ${m[3]}`;
            } else {
                parsedRes = "Invalid price query"
            }
        } else {
            parsedRes = "Invalid price query"
        }
    }
    return (
      <i className={searchValue.match(/price:/) ? "" : "invisible"}>keyword: {isEmpty(query) ? "none" : query} AND {parsedRes}</i>
    );
}

export default function AppAutoComplete({
    searchValue,
    setSearchValue,
    placeholder,
}: {
    searchValue: string
    setSearchValue: (value: string) => void
    placeholder: string
}) {
    const [autoCompleteOptions, setAutoCompleteOptions] = useState<
        { value: string }[]
    >([])
    const [debounced] = useDebounce(searchValue, 100)

    useEffect(() => {
        const fetchAutoCompleteData = async () => {
            try {
                const response = await axios.get('/api/auto-complete', {
                    params: { prefix: searchValue },
                })
                setAutoCompleteOptions(
                    response.data.map((item: string) => ({ value: item }))
                )
            } catch (error) {
                console.error('Fetch error:', error)
            }
        }
        fetchAutoCompleteData()
    }, [debounced])
    return (
      <div className="flex-grow">
          <AutoComplete
            placeholder={placeholder}
            onSearch={(value) => {
                setSearchValue(value);
            }}
            className='w-full'
            options={autoCompleteOptions}
            value={searchValue}
            onSelect={(value) => {
                setSearchValue(value);
            }}
            showSearch
          />
          <SearchValidation searchValue={searchValue} />
      </div>
)
}
