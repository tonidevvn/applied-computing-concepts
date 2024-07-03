import { Button, Card } from 'antd'
import axios from 'axios'
import { useEffect, useState } from 'react'

export default function AppSpellChecking({
    searchValue,
    setSearchValue,
}: {
    searchValue: string
    setSearchValue: (value: string) => void
}) {
    const [spellCheckOptions, setSpellCheckOptions] = useState<string[]>([])

    useEffect(() => {
        const fetchAutoCompleteData = async () => {
            try {
                const response = await axios.get('/api/spell-checking', {
                    params: { word: searchValue },
                })
                setSpellCheckOptions(response.data)
            } catch (error) {
                console.error('Fetch error:', error)
            }
        }
        fetchAutoCompleteData()
    }, [searchValue])
    return (
        <Card title='Spell Checking - Do You Mean'>
            {spellCheckOptions.map((option, i) => (
                <Button key={i} onClick={() => setSearchValue(option)}>
                    {option}
                </Button>
            ))}
        </Card>
    )
}
