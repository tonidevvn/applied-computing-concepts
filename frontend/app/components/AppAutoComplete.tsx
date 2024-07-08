import { AutoComplete } from 'antd'
import axios from 'axios'
import { useEffect, useState } from 'react'
import { useDebounce } from 'use-debounce'

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
    const [debounced ] = useDebounce(searchValue, 1000);

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
        <AutoComplete
            placeholder={placeholder}
            onSearch={(value) => setSearchValue(value)}
            style={{ marginBottom: 24, width: '200px' }}
            options={autoCompleteOptions}
            value={searchValue}
            onSelect={(value) => setSearchValue(value)}
            showSearch
        />
    )
}
